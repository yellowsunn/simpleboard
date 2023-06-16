package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import org.springframework.kafka.core.KafkaTemplate

class GlobalKafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    eventSendFailureRepository: EventSendFailureRepository,
) : DefaultKafkaEventProducer(eventSendFailureRepository) {

    fun send(topic: String, data: Any) {
        kafkaTemplate.sendData(topic, data)
    }
}
