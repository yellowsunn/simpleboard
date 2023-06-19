package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.command.domain.comment.Comment
import com.yellowsunn.boardservice.command.repository.CommentLikeRepository
import com.yellowsunn.boardservice.command.repository.CommentRepository
import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import com.yellowsunn.boardservice.query.repository.CommentDocumentRepository
import org.springframework.stereotype.Service

@Service
class CommentSyncService(
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val commentDocumentRepository: CommentDocumentRepository,
) {
    fun syncCommentDocument(commentId: Long) {
        val comment: Comment = commentRepository.findById(commentId, true) ?: return
        val likeCount: Long = commentLikeRepository.countByCommentId(commentId)

        val commentDocument = CommentDocument(
            commentId = comment.id,
            articleId = comment.articleId,
            userId = comment.userId,
            content = comment.content ?: "",
            imageUrl = comment.imageUrl,
            likeCount = likeCount,
            savedAt = comment.createdAt,
            parentCommentId = comment.parentCommentId,
            baseCommentId = comment.baseCommentId,
            isDeleted = comment.isDeleted,
        )

        commentDocumentRepository.upsertByCommentId(commentId, commentDocument)
    }
}
