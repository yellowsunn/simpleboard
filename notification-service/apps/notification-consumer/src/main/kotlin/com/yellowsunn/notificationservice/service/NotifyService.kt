package com.yellowsunn.notificationservice.service

import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.message.NotificationMessage
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.stereotype.Service

@Service
class NotifyService(
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
