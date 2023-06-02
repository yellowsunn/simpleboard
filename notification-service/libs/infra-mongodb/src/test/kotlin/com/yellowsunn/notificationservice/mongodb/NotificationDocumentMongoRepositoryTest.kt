package com.yellowsunn.notificationservice.mongodb

import com.yellowsunn.notificationservice.domain.NotificationDocument
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

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
        val notificationDocument = NotificationDocument(
            userId = 1L,
            tag = "댓글 알림",
            title = "게시글에 댓글이 달렸습니다.",
            content = "댓글입니다.",
            contentLink = "http://example.com/comments",
        )

        val savedDocument: NotificationDocument = sut.save(notificationDocument)

        assertThat(savedDocument.id).isNotBlank()
    }
}
