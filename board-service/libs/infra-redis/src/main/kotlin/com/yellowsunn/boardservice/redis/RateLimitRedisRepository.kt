package com.yellowsunn.boardservice.redis

import com.yellowsunn.boardservice.command.repository.RateLimitCacheRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RateLimitRedisRepository(
    private val stringRedisTemplate: StringRedisTemplate,
) : RateLimitCacheRepository {

    override fun acquireJustOne(key: String, duration: Duration): Boolean {
        return stringRedisTemplate.opsForValue()
            .setIfAbsent(generateKey(key), true.toString(), duration) ?: false
    }

    private fun generateKey(key: String): String {
        return "rate-limit:$key"
    }
}
