package com.yellowsunn.boardservice.redis.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import redis.embedded.RedisServer

@Configuration
class TestRedisConfiguration {
    private companion object {
        private const val REDIS_PORT = 63790
    }

    private val redisServer: RedisServer = RedisServer.builder()
        .port(REDIS_PORT)
        .setting("maxmemory 100mb")
        .setting("maxmemory-policy allkeys-lru")
        .setting("maxmemory-samples 10")
        .build()

    @PostConstruct
    fun startRedisServer() {
        redisServer.start()
    }

    @PreDestroy
    fun stopRedisServer() {
        redisServer.stop()
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory("localhost", REDIS_PORT)
    }

    @Bean
    fun stringRedisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
    ): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }
}
