package com.yellowsunn.boardservice.config

import com.yellowsunn.boardservice.command.message.producer.GlobalMessageProducer
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import com.yellowsunn.boardservice.dto.EventSendFailureDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.ZonedDateTime
import javax.sql.DataSource

@Configuration
class EventSendFailureRetryJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val globalMessageProducer: GlobalMessageProducer,
    private val eventSendFailureRepository: EventSendFailureRepository,
    private val dataSource: DataSource,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val CHUNK_SIZE = 1_000
    }

    @Bean
    fun job(): Job {
        return JobBuilder("eventSendFailureRetryJob", jobRepository)
            .start(step())
            .build()
    }

    @Bean
    fun step(): Step {
        return StepBuilder("retryStep", jobRepository)
            .chunk<EventSendFailureDto, EventSendFailureDto>(CHUNK_SIZE, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    @StepScope
    fun reader(): JdbcPagingItemReader<EventSendFailureDto> {
        return JdbcPagingItemReaderBuilder<EventSendFailureDto>()
            .name("jdbcReader")
            .pageSize(CHUNK_SIZE)
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper { rs, _ ->
                EventSendFailureDto(
                    id = rs.getLong("id"),
                    topic = rs.getString("topic"),
                    data = rs.getString("data"),
                    isUsed = rs.getBoolean("is_used"),
                    modifiedAt = ZonedDateTime.now(),
                )
            }
            .queryProvider(customQueryProvider())
            .build()
    }

    @Bean
    @StepScope
    fun processor(): ItemProcessor<EventSendFailureDto, EventSendFailureDto> {
        return ItemProcessor<EventSendFailureDto, EventSendFailureDto> { dto ->
            globalMessageProducer.send(dto.topic, dto.data).thenAccept {
                if (it == null) {
                    logger.info("발행 실패한 이벤트 재발행. topic={}, data={}", dto.topic, dto.data)
                } else {
                    // 재발행 실패한 경우
                    eventSendFailureRepository.save(it)
                }
            }
            dto
        }
    }

    @Bean
    @StepScope
    fun writer(): JdbcBatchItemWriter<EventSendFailureDto> {
        return JdbcBatchItemWriterBuilder<EventSendFailureDto>()
            .dataSource(dataSource)
            .sql("update event_send_failure set is_used = true, modified_at = :modifiedAt where id = :id")
            .beanMapped()
            .build()
    }

    private fun customQueryProvider(): PagingQueryProvider {
        val queryProviderFactoryBean = SqlPagingQueryProviderFactoryBean().apply {
            setDataSource(dataSource)
            setSelectClause("select id, topic, data, is_used, created_at, modified_at")
            setFromClause("from event_send_failure")
            setWhereClause("where is_used = false")
            setSortKeys(mapOf("id" to Order.ASCENDING))
        }

        return queryProviderFactoryBean.`object`
    }
}
