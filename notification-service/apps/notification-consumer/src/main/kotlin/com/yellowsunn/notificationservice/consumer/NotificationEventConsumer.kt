package com.yellowsunn.notificationservice.consumer

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.common.constant.KafkaTopicConst.NOTIFICATION_TOPIC
import com.yellowsunn.notificationservice.message.NotificationMessage
import com.yellowsunn.notificationservice.service.NotifyService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class NotificationEventConsumer(
    private val notifyService: NotifyService,
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false),
) {
    @KafkaListener(
        topics = [NOTIFICATION_TOPIC],
        groupId = "notification-group",
    )
    fun notify(record: ConsumerRecord<String, String>) {
        val message: NotificationMessage =
            objectMapper.readValue(record.value(), NotificationMessage::class.java)

        notifyService.notify(message)
    }
}
