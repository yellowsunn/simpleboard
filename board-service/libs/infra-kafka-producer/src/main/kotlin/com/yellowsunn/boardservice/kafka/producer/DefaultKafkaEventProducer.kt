package com.yellowsunn.boardservice.kafka.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import java.util.concurrent.CompletableFuture

abstract class DefaultKafkaEventProducer {
    private val log = KotlinLogging.logger { }
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    protected fun KafkaTemplate<String, String>.sendData(
        topic: String,
        data: Any,
    ): CompletableFuture<EventSendFailure?> {
        val jsonData: String = objectMapper.writeValueAsString(data)

        return this.send(topic, jsonData)
            .handle { _, e ->
                if (e != null) {
                    log.error("Failed to produce kafka data. topic={}, data={}", topic, data, e)
                    EventSendFailure(topic = topic, data = jsonData)
                } else {
                    null
                }
            }
    }
}
