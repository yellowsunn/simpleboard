package com.yellowsunn.apigatewayservice.filter

import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import com.yellowsunn.common.exception.JwtTokenParseException
import com.yellowsunn.common.utils.token.AccessTokenParser
import com.yellowsunn.common.utils.token.AccessTokenPayload
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderFilter(
    private val accessTokenParser: AccessTokenParser,
) : AbstractGatewayFilterFactory<Any>() {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private companion object {
        const val BEARER = "bearer "
    }

    override fun apply(config: Any?) = GatewayFilter { exchange, chain ->
        val request: ServerHttpRequest = exchange.request
        val authorization = getAuthorizationValue(request)
        val userId: Long? = if (authorization.isNotBlank()) {
            getUserIdByAccessToken(authorization)
        } else {
            null
        }

        val changedRequest = exchange.request.mutate()
            .header(USER_ID, userId?.toString())
            .build()

        chain.filter(exchange.mutate().request(changedRequest).build())
    }

    private fun getAuthorizationValue(request: ServerHttpRequest): String {
        val authorization: String = request.headers[HttpHeaders.AUTHORIZATION]?.getOrNull(0)
            ?: return ""

        if (authorization.startsWith(BEARER, ignoreCase = true).not()) {
            return ""
        }
        return authorization.replace(BEARER, "", ignoreCase = true).trim()
    }

    private fun getUserIdByAccessToken(encodedAccessToken: String): Long? {
        return try {
            val accessTokenPayload: AccessTokenPayload = accessTokenParser.parseEncodedToken(encodedAccessToken)
            accessTokenPayload.id
        } catch (e: JwtTokenParseException) {
            logger.warn("토큰 값을 조회하는데 실패하였습니다.")
            null
        }
    }
}
