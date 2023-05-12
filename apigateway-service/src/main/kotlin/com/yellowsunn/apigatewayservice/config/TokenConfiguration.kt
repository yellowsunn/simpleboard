package com.yellowsunn.apigatewayservice.config

import com.yellowsunn.common.utils.token.AccessTokenParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenConfiguration {
    @Bean
    fun accessTokenParser(
        @Value("\${token.access.secret}") secret: String,
    ): AccessTokenParser {
        return AccessTokenParser(secret)
    }
}
