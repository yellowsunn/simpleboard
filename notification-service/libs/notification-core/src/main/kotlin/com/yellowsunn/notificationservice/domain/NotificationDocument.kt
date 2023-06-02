package com.yellowsunn.notificationservice.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "notifications")
class NotificationDocument(
    val userId: Long,
    val tag: String,
    val title: String,
    val content: String,
    val contentLink: String? = null,
) : BaseDocumentEntity() {
    var readAt: ZonedDateTime? = null
        private set
}
