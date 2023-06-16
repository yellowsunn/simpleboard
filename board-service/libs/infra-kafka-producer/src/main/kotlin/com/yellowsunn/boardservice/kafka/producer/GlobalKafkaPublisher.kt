package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import com.yellowsunn.boardservice.command.message.producer.GlobalMessageProducer
import java.util.concurrent.CompletableFuture
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class GlobalKafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : GlobalMessageProducer,
    DefaultKafkaEventProducer() {

    override fun send(topic: String, data: Any): CompletableFuture<EventSendFailure?> {
        return kafkaTemplate.sendData(topic, data)
    }
}
