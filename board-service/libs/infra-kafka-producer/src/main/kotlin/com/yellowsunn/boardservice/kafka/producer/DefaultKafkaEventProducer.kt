package com.yellowsunn.boardservice.kafka.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import com.yellowsunn.boardservice.command.event.producer.data.ProducerData
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

abstract class DefaultKafkaEventProducer(
    private val eventSendFailureRepository: EventSendFailureRepository,
) {
    private val log = KotlinLogging.logger { }
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    protected fun KafkaTemplate<String, String>.sendData(
        topic: String,
        data: Any,
    ): CompletableFuture<SendResult<String, String>> {
        val jsonData: String = objectMapper.writeValueAsString(data)

        return this.send(topic, jsonData)
            .whenComplete { _, e ->
                if (e != null) {
                    log.error("Failed to produce kafka data. topic={}, data={}", topic, data, e)
                    val eventSendFailure = EventSendFailure(
                        topic = topic,
                        data = jsonData,
                    )
                    eventSendFailureRepository.save(eventSendFailure)
                }
            }
    }
}
