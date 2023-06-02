package com.yellowsunn.notificationservice.repository

import com.yellowsunn.notificationservice.domain.NotificationDocument

interface NotificationDocumentRepository {
    fun save(notificationDocument: NotificationDocument): NotificationDocument
    fun findByUserId(userId: Long): NotificationDocument?
}
