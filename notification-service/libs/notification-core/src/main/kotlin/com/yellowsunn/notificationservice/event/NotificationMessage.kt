package com.yellowsunn.notificationservice.event

import com.yellowsunn.common.notification.NotificationData

data class NotificationMessage(
    val userId: Long,
    val title: String,
    val content: String,
    val data: NotificationData,
)
