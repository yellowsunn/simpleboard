package com.yellowsunn.boardservice.command.event.producer

import com.yellowsunn.common.notification.NotificationEvent

interface NotificationEventProducer {
    fun notify(notificationEvent: NotificationEvent)
}
