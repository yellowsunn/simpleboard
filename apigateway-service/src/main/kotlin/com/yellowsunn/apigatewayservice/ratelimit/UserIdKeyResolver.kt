package com.yellowsunn.apigatewayservice.ratelimit

import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class UserIdKeyResolver : KeyResolver {
    override fun resolve(exchange: ServerWebExchange): Mono<String> {
        val userId = exchange.request.headers.getFirst(USER_ID)!!
        return Mono.just(userId)
    }
}
