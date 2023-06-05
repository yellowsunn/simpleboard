package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.mongodb.MongoIntegrationTest
import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.ZonedDateTime

class CommentMongoRepositoryIntegrationTest : MongoIntegrationTest() {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var delegate: CommentMongoRepositoryDelegate

    private lateinit var commentMongoRepository: CommentMongoRepository

    @BeforeEach
    fun setUp() {
        commentMongoRepository = CommentMongoRepository(delegate, mongoTemplate)
    }

    @Test
    fun upsertByCommentId() {
        val commentDocument = getTestComment(1L, 1L)

        val savedCommentDocument = commentMongoRepository.upsertByCommentId(1L, commentDocument)

        assertThat(savedCommentDocument?.id).isNotNull
    }

    private fun getTestComment(articleId: Long, commentId: Long) = CommentDocument(
        articleId = articleId,
        commentId = commentId,
        userId = 1L,
        content = "content",
        savedAt = ZonedDateTime.now(),
        baseCommentId = commentId,
    )
}
