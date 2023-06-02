package com.yellowsunn.notificationservice.domain

import com.yellowsunn.common.notification.NotificationData
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "notifications")
class NotificationDocument(
    val userId: Long,
    val title: String,
    val content: String,
    val data: NotificationData,
) : BaseDocumentEntity() {
    var readAt: ZonedDateTime? = null
        private set
}
