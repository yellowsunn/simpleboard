package com.yellowsunn.notificationservice.consumer

import com.yellowsunn.common.constant.KafkaTopicConst.NOTIFICATION_TOPIC
import com.yellowsunn.common.notification.NotificationEvent
import com.yellowsunn.notificationservice.service.NotificationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class NotificationEventConsumer(
    private val notificationService: NotificationService,
) {
    @KafkaListener(
        topics = [NOTIFICATION_TOPIC],
        groupId = "notification-group",
    )
    fun notify(@Payload event: NotificationEvent) {
        notificationService.notify(event)
    }
}
