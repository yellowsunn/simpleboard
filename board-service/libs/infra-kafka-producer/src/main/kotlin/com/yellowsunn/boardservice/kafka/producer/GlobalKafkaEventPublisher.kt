package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.GlobalEventProducer
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class GlobalKafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    eventSendFailureRepository: EventSendFailureRepository,
) : GlobalEventProducer,
    DefaultKafkaEventProducer(eventSendFailureRepository) {

    override fun send(topic: String, data: Any) {
        kafkaTemplate.sendData(topic, data)
    }
}
