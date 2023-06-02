package com.yellowsunn.notificationservice.service

import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.event.NotificationMessage
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationDocumentRepository: NotificationDocumentRepository,
) {
    fun notify(message: NotificationMessage) {
        val notificationDocument = NotificationDocument(
            userId = message.userId,
            title = message.title,
            content = message.content,
            data = message.data,
        )

        notificationDocumentRepository.save(notificationDocument)
    }
}
