package com.yellowsunn.boardservice.command.event.producer

import com.yellowsunn.boardservice.command.event.producer.data.notification.NotificationMessage

interface NotificationMessageProducer {
    fun notify(message: NotificationMessage)
}
