package com.yellowsunn.boardservice.http.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.temporal.ChronoUnit

@Configuration
class HttpClientConfiguration {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder
            .setConnectTimeout(Duration.of(1, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .build()
    }
}
