package com.yellowsunn.notificationservice.config

import com.yellowsunn.notificationservice.service.NotificationService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun notificationService() = mockk<NotificationService>()
}
