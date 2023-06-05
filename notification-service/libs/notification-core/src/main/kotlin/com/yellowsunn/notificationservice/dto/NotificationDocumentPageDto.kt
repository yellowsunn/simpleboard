package com.yellowsunn.notificationservice.dto

import com.yellowsunn.common.notification.NotificationData
import com.yellowsunn.notificationservice.domain.NotificationDocument
import org.springframework.data.domain.Page

data class NotificationDocumentPageDto(
    val notifications: List<NotificationDocumentDto>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val numberOfElements: Int,
    val totalElements: Long,
) {
    data class NotificationDocumentDto(
        val userId: Long,
        val title: String,
        val content: String,
        val data: NotificationData,
    )

    companion object {
        fun from(notificationPage: Page<NotificationDocument>): NotificationDocumentPageDto {
            val notifications: List<NotificationDocumentDto> = notificationPage.content.map {
                NotificationDocumentDto(
                    userId = it.userId,
                    title = it.title,
                    content = it.content,
                    data = it.data,
                )
            }

            return NotificationDocumentPageDto(
                notifications = notifications,
                page = notificationPage.number + 1,
                size = notifications.size,
                totalPages = notificationPage.totalPages,
                numberOfElements = notificationPage.numberOfElements,
                totalElements = notificationPage.totalElements,
            )
        }
    }
}
