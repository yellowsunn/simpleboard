package com.yellowsunn.boardservice.query.dto

import com.yellowsunn.boardservice.common.domain.user.SimpleUser
import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import org.springframework.data.domain.Page
import java.time.ZonedDateTime

data class CommentDocumentPageDto(
    val comments: List<CommentDocumentDto>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val numberOfElements: Int,
    val totalElements: Long,
) {
    data class CommentDocumentDto(
        val commentId: Long,
        val articleId: Long,
        val parentCommentId: Long?,
        val baseCommentId: Long,
        val content: String,
        val imageUrl: String?,
        val likeCount: Long,
        val nickName: String,
        val userThumbnail: String,
        val savedAt: ZonedDateTime,
    )

    companion object {
        fun from(
            documentPage: Page<CommentDocument>,
            users: Map<Long, SimpleUser>,
        ): CommentDocumentPageDto {
            val comments = documentPage.content.map {
                convertCommentDocumentDto(it, users[it.userId])
            }
            return CommentDocumentPageDto(
                comments = comments,
                page = documentPage.number + 1,
                size = documentPage.size,
                totalPages = documentPage.totalPages,
                numberOfElements = documentPage.numberOfElements,
                totalElements = documentPage.totalElements,
            )
        }

        private fun convertCommentDocumentDto(
            document: CommentDocument,
            user: SimpleUser?,
        ) = CommentDocumentDto(
            commentId = document.commentId,
            articleId = document.articleId,
            parentCommentId = document.parentCommentId,
            baseCommentId = document.baseCommentId,
            content = document.content,
            imageUrl = document.imageUrl,
            likeCount = document.likeCount,
            nickName = user?.nickName ?: "",
            userThumbnail = user?.thumbnail ?: "",
            savedAt = document.savedAt,
        )
    }
}
