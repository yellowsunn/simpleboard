package com.yellowsunn.boardservice.query.domain.comment

import com.yellowsunn.boardservice.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "comments")
class CommentDocument(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
    var content: String,
    var imageUrl: String? = "",
    var likeCount: Long = 0L,
    var isDeleted: Boolean = false,
    val savedAt: ZonedDateTime,
    val parentCommentId: Long? = null,
    val baseCommentId: Long,
) : BaseDocumentEntity()
