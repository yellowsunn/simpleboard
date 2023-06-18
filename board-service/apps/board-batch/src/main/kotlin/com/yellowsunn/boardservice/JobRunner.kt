package com.yellowsunn.boardservice

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JobRunner(
    private val jobLauncher: JobLauncher,
    private val applicationContext: ApplicationContext,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val jobName = args.getOptionValues("job.name").first()
        val job: Job = applicationContext.getBean(jobName, Job::class.java)

        val jobParameters = JobParametersBuilder()
            .addDate("date", Date())
            .toJobParameters()

        jobLauncher.run(job, jobParameters)
    }
}
