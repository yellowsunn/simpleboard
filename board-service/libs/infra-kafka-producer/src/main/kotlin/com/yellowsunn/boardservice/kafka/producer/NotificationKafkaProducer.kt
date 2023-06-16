package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.NotificationMessageProducer
import com.yellowsunn.boardservice.command.event.producer.data.notification.NotificationMessage
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import com.yellowsunn.common.constant.KafkaTopicConst.NOTIFICATION_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NotificationKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    eventSendFailureRepository: EventSendFailureRepository,
) : NotificationMessageProducer,
    DefaultKafkaEventProducer(eventSendFailureRepository) {

    override fun notify(message: NotificationMessage) {
        kafkaTemplate.sendData(NOTIFICATION_TOPIC, message)
    }
}
