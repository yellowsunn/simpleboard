package com.yellowsunn.boardservice.command.message.producer

import com.yellowsunn.boardservice.command.message.producer.data.notification.NotificationMessage

interface NotificationMessageProducer {
    fun notify(message: NotificationMessage)
}
