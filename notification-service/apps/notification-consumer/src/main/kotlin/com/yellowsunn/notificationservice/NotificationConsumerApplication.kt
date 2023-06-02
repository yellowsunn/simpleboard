package com.yellowsunn.notificationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotificationConsumerApplication

fun main(args: Array<String>) {
    runApplication<NotificationConsumerApplication>(*args)
}
