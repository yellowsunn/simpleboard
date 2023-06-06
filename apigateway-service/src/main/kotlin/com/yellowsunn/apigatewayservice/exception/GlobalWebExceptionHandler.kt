package com.yellowsunn.apigatewayservice.exception

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.common.exception.ExpiredAccessTokenException
import com.yellowsunn.common.response.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class GlobalWebExceptionHandler : ErrorWebExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(exchange: ServerWebExchange, e: Throwable): Mono<Void> {
        val errorResponse = if (e is ExpiredAccessTokenException) {
            ErrorResponse.accessTokenExpired()
        } else {
            logger.error("Unknown error. message={}", e.message, e)
            ErrorResponse.unknownError()
        }

        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        exchange.response.rawStatusCode = errorResponse.status

        val bytes: ByteArray = jacksonObjectMapper().writeValueAsBytes(errorResponse)
        val buffer: DataBuffer = exchange.response.bufferFactory().wrap(bytes)
        return exchange.response.writeWith(Flux.just(buffer))
    }
}
