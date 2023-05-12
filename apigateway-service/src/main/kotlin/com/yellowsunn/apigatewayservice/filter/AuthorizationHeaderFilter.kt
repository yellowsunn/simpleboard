package com.yellowsunn.apigatewayservice.filter

import com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER
import com.yellowsunn.common.utils.token.AccessTokenParser
import com.yellowsunn.common.utils.token.AccessTokenPayload
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderFilter(
    private val accessTokenParser: AccessTokenParser,
) : AbstractGatewayFilterFactory<Any>() {
    override fun apply(config: Any?) = GatewayFilter { exchange, chain ->
        val request: ServerHttpRequest = exchange.request

        val authorizationHeader: String? = request.headers[HttpHeaders.AUTHORIZATION]?.getOrNull(0)
        val userId: String = if (authorizationHeader.isNullOrBlank()) {
            ""
        } else {
            val jwt = authorizationHeader.replace("bearer", "", true)
            val accessTokenPayload: AccessTokenPayload = accessTokenParser.parseEncodedToken(jwt)
            accessTokenPayload.uuid
        }

        val changedRequest = exchange.request.mutate()
            .header(USER_UUID_HEADER, userId)
            .build()

        chain.filter(exchange.mutate().request(changedRequest).build())
    }
}
