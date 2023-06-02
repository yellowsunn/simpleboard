package com.yellowsunn.notificationservice.service

import com.yellowsunn.common.notification.NotificationEvent
import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationDocumentRepository: NotificationDocumentRepository,
) {
    fun notify(event: NotificationEvent) {
        val notificationDocument = NotificationDocument(
            userId = event.userId,
            tag = event.tag,
            title = event.title,
            content = event.content,
            contentLink = event.contentLink,
        )

        notificationDocumentRepository.save(notificationDocument)
    }
}
