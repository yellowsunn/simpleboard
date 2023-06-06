package com.yellowsunn.imageservice.config

import com.yellowsunn.imageservice.service.ImageService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun imageService() = mockk<ImageService>()
}
