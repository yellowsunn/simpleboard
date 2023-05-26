package com.yellowsunn.boardservice.http.client.user

import com.yellowsunn.boardservice.common.domain.user.SimpleUser
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class UserRestHttpClient(
    private val restTemplate: RestTemplate,
    @Value("\${micro-services.user-service-url}") private val userServiceUrl: String,
) : UserHttpClient {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun findUserByUserId(userId: Long): User? {
        val uri = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
            .path("/api/internal/v1/users/{userId}")
            .buildAndExpand(userId)
            .toUri()

        return try {
            restTemplate.getForObject(uri, User::class.java)
        } catch (e: Exception) {
            logger.warn("Failed to get user. userId={}", userId, e)
            null
        }
    }

    override fun findUsersByIds(userIds: List<Long>): Map<Long, SimpleUser> {
        val uri = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
            .path("/api/internal/v1/users")
            .queryParam("id", userIds)
            .build()
            .toUri()

        val simpleUsers = try {
            val responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<SimpleUser>>() {},
            )
            responseEntity.body ?: emptyList()
        } catch (e: Exception) {
            logger.warn("Failed to get users. userIds={}", userIds, e)
            emptyList()
        }

        return simpleUsers.associateBy { it.userId }
    }
}
