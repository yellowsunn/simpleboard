package com.yellowsunn.boardservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.script.RedisScript

@Configuration
class LuaScriptConfig {
    @Bean
    fun viewCountPopScript(): RedisScript<Long> {
        val classPathResource = ClassPathResource("ViewCountPop.lua")
        return RedisScript.of(classPathResource, Long::class.java)
    }
}
