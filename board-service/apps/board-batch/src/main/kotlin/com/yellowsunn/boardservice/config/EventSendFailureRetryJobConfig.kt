package com.yellowsunn.boardservice.config

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import com.yellowsunn.boardservice.command.event.producer.GlobalEventProducer
import jakarta.persistence.EntityManagerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class EventSendFailureRetryJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val globalEventProducer: GlobalEventProducer,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val CHUNK_SIZE = 1_000
    }

    @Bean
    fun helloJob(): Job {
        return JobBuilder("eventSendFailureRetryJob", jobRepository)
            .start(step())
            .build()
    }

    @Bean
    fun step(): Step {
        return StepBuilder("retryStep", jobRepository)
            .chunk<EventSendFailure, EventSendFailure>(CHUNK_SIZE, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    @StepScope
    fun reader(): JpaPagingItemReader<EventSendFailure> {
        return JpaPagingItemReader<EventSendFailure>().apply {
            name = "event_send_failure_reader"
            pageSize = CHUNK_SIZE
            setQueryString("select e from EventSendFailure e where e.isUsed = false")
            setEntityManagerFactory(entityManagerFactory)
        }
    }

    @Bean
    @StepScope
    fun processor(): ItemProcessor<EventSendFailure, EventSendFailure> {
        return ItemProcessor<EventSendFailure, EventSendFailure> {
            globalEventProducer.send(it.topic, it.data)
            it.use()

            logger.info("발행 실패한 이벤트 재발행. topic={}, data={}", it.topic, it.data)
            it
        }
    }

    @Bean
    @StepScope
    fun writer(): JpaItemWriter<EventSendFailure> {
        return JpaItemWriterBuilder<EventSendFailure>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }
}
