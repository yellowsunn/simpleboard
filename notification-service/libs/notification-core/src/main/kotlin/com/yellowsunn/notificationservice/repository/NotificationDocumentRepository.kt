package com.yellowsunn.notificationservice.repository

import com.yellowsunn.notificationservice.domain.NotificationDocument
import org.springframework.data.domain.Page
import java.time.ZonedDateTime

interface NotificationDocumentRepository {
    fun save(notificationDocument: NotificationDocument): NotificationDocument
    fun findByUserId(userId: Long): NotificationDocument?
    fun findUserNotifications(userId: Long, page: Int, size: Int): Page<NotificationDocument>
    fun existUnreadNotifications(userId: Long): Boolean
    fun updateReadAtByUserId(userId: Long, readAt: ZonedDateTime): Long
}
