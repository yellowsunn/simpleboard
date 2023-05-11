package com.yellowsunn.userservice.redis.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {
    private final RedisServer redisServer;

    public TestRedisConfiguration() {
        this.redisServer = RedisServer.builder()
                .setting("maxmemory 100mb")
                .setting("maxmemory-policy allkeys-lru")
                .setting("maxmemory-samples 10")
                .build();
    }

    @PostConstruct
    void startRedisServer() {
        redisServer.start();
    }

    @PreDestroy
    void stopRedisServer() {
        redisServer.stop();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }
}
