package com.yellowsunn.apigatewayservice.filter

import com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER
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
        val authorizationHeader: String? = request.headers[HttpHeaders.AUTHORIZATION]?.getOrNull(0)

        val isStartsWithBearer = authorizationHeader?.startsWith(BEARER, ignoreCase = true) ?: false
        if (isStartsWithBearer.not()) {
            chain.filter(exchange)
        } else {
            val jwt = authorizationHeader?.replace(BEARER, "", ignoreCase = true)?.trim() ?: ""
            val accessTokenPayload: AccessTokenPayload = accessTokenParser.parseEncodedToken(jwt)
            val changedRequest = exchange.request.mutate()
                .header(USER_UUID_HEADER, accessTokenPayload.uuid)
                .build()

            chain.filter(exchange.mutate().request(changedRequest).build())
        }
    }
}
