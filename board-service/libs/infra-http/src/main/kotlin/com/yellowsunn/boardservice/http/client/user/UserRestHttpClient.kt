package com.yellowsunn.boardservice.http.client.user

import com.yellowsunn.boardservice.domain.user.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class UserRestHttpClient(
    private val restTemplate: RestTemplate,
    @Value("\${micro-services.user-service-url}") private val userServiceUrl: String,
) : UserHttpClient {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun findUserByUserUUID(userUUID: String): User? {
        val uri = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
            .path("/api/internal/v1/users/uuid/{userUUID}")
            .buildAndExpand(userUUID)
            .toUri()

        return try {
            restTemplate.getForObject(uri, User::class.java)
        } catch (e: Exception) {
            logger.warn("Failed to get user. uuid={}", userUUID, e)
            null
        }
    }
}