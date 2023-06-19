package com.yellowsunn.notificationservice.service

import com.yellowsunn.common.utils.PageUtils
import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.dto.NotificationDocumentPageDto
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class NotificationService(
    private val notificationDocumentRepository: NotificationDocumentRepository,
) {
    private companion object {
        private const val MAX_PAGE_SIZE = 100
    }

    fun findUserNotifications(userId: Long, page: Int, size: Int): NotificationDocumentPageDto {
        val curPage = PageUtils.currentPage(page - 1)
        val curSize = PageUtils.currentSize(size, MAX_PAGE_SIZE)

        val notificationPage: Page<NotificationDocument> =
            notificationDocumentRepository.findUserNotifications(userId, curPage, curSize)

        return NotificationDocumentPageDto.from(notificationPage)
    }

    fun readUserNotifications(userId: Long): Long {
        return notificationDocumentRepository.updateReadAtByUserId(userId, ZonedDateTime.now())
    }

    fun existUserUnreadNotifications(userId: Long): Boolean {
        return notificationDocumentRepository.existUnreadNotifications(userId)
    }
}
