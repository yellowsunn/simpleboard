package com.yellowsunn.notificationservice.repository

import com.yellowsunn.notificationservice.domain.NotificationDocument
import org.springframework.data.domain.Page

interface NotificationDocumentRepository {
    fun save(notificationDocument: NotificationDocument): NotificationDocument
    fun findByUserId(userId: Long): NotificationDocument?
    fun findUserNotifications(userId: Long, page: Int, size: Int): Page<NotificationDocument>
}
