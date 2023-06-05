package com.yellowsunn.notificationservice.service

import com.yellowsunn.common.utils.PageUtils
import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.dto.NotificationDocumentPageDto
import com.yellowsunn.notificationservice.event.NotificationMessage
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationDocumentRepository: NotificationDocumentRepository,
) {
    private companion object {
        private const val MAX_PAGE_SIZE = 100
    }

    fun notify(message: NotificationMessage) {
        val notificationDocument = NotificationDocument(
            userId = message.userId,
            title = message.title,
            content = message.content,
            data = message.data,
        )

        notificationDocumentRepository.save(notificationDocument)
    }

    fun findNotifications(userId: Long, page: Int, size: Int): NotificationDocumentPageDto {
        val curPage = PageUtils.currentPage(page - 1)
        val curSize = PageUtils.currentSize(size, MAX_PAGE_SIZE)

        val notificationPage: Page<NotificationDocument> =
            notificationDocumentRepository.findUserNotifications(userId, curPage, curSize)

        return NotificationDocumentPageDto.from(notificationPage)
    }
}
