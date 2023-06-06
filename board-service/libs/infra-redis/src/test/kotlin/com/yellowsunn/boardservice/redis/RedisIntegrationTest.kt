package com.yellowsunn.boardservice.redis

import com.yellowsunn.boardservice.redis.config.TestRedisConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisConfiguration::class])
abstract class RedisIntegrationTest
