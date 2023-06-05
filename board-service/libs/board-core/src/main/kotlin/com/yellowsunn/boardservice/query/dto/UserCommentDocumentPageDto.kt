package com.yellowsunn.boardservice.query.dto

import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import org.springframework.data.domain.Page
import java.time.ZonedDateTime

class UserCommentDocumentPageDto(
    val comments: List<UserCommentDocumentDto>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val numberOfElements: Int,
    val totalElements: Long,
) {
    data class UserCommentDocumentDto(
        val commentId: Long,
        val articleId: Long,
        val content: String,
        val imageUrl: String?,
        val savedAt: ZonedDateTime,
    )

    companion object {
        fun from(
            documentPage: Page<CommentDocument>,
        ): UserCommentDocumentPageDto {
            val comments = documentPage.content.map {
                UserCommentDocumentDto(
                    commentId = it.commentId,
                    articleId = it.articleId,
                    content = it.content,
                    imageUrl = it.imageUrl,
                    savedAt = it.savedAt,
                )
            }

            return UserCommentDocumentPageDto(
                comments = comments,
                page = documentPage.number + 1,
                size = documentPage.size,
                totalPages = documentPage.totalPages,
                numberOfElements = documentPage.numberOfElements,
                totalElements = documentPage.totalElements,
            )
        }
    }
}
