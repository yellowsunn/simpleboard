package com.yellowsunn.boardservice.config

import com.yellowsunn.boardservice.service.ArticleViewCountSyncService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ViewCountSyncJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val articleViewCountSyncService: ArticleViewCountSyncService,
) {
    @Bean
    fun viewCountSyncJob(): Job {
        return JobBuilder("viewCountSyncJob", jobRepository)
            .start(updateViewCountStep())
            .build()
    }

    @Bean
    fun updateViewCountStep(): Step {
        return StepBuilder("updateViewCountStep", jobRepository)
            .tasklet({ _, _ ->
                articleViewCountSyncService.updateArticleViewCount()
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }
}
