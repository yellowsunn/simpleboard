package com.yellowsunn.notificationservice.mongodb

import com.yellowsunn.common.notification.CommentNotificationData
import com.yellowsunn.notificationservice.domain.NotificationDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.ZonedDateTime

class NotificationDocumentMongoRepositoryTest : MongoIntegrationTest() {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var delegate: NotificationDocumentMongoRepositoryDelegate

    private lateinit var sut: NotificationDocumentMongoRepository

    @BeforeEach
    fun setUp() {
        sut = NotificationDocumentMongoRepository(
            delegate = delegate,
            mongoTemplate = mongoTemplate,
        )
    }

    @Test
    fun save() {
        val notificationDocument = getTestNotification()

        val savedDocument: NotificationDocument = sut.save(notificationDocument)

        assertThat(savedDocument.id).isNotBlank()
    }

    @Test
    fun findUserNotifications() {
        repeat(10) {
            val notificationDocument = getTestNotification()
            sut.save(notificationDocument)
        }

        val notifications: Page<NotificationDocument> = sut.findUserNotifications(1L, 1, 3)

        assertThat(notifications.totalElements).isEqualTo(10L)
        assertThat(notifications.totalPages).isEqualTo(4L)
        assertThat(notifications.content.map { it.createdAt }).isSortedAccordingTo(Comparator.reverseOrder())
    }

    @Test
    fun readNotifications() {
        repeat(10) {
            val notificationDocument = getTestNotification()
            sut.save(notificationDocument)
        }

        var isUnread: Boolean = sut.existUnreadNotifications(1L)
        assertThat(isUnread).isTrue

        val updatedCount = sut.updateReadAtByUserId(1L, ZonedDateTime.now())
        assertThat(updatedCount).isEqualTo(10L)

        isUnread = sut.existUnreadNotifications(1L)
        assertThat(isUnread).isFalse
    }

    private fun getTestNotification() = NotificationDocument(
        userId = 1L,
        title = "게시글에 댓글이 달렸습니다.",
        content = "댓글 입니다.",
        data = CommentNotificationData(1L, 1L),
    )
}
