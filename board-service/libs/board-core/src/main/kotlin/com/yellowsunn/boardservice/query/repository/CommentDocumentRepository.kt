package com.yellowsunn.boardservice.query.repository

import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import org.springframework.data.domain.Page

interface CommentDocumentRepository {
    fun upsertByCommentId(commentId: Long, entity: CommentDocument): CommentDocument?
    fun findByCommentId(commentId: Long): CommentDocument?
    fun findComments(articleId: Long, page: Int, size: Int): Page<CommentDocument>
    fun findOffsetByCommentId(articleId: Long, baseCommentId: Long, commentId: Long): Long
}
