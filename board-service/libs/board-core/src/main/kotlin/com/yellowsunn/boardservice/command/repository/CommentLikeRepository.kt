package com.yellowsunn.boardservice.command.repository

import com.yellowsunn.boardservice.command.domain.comment.CommentLike
import com.yellowsunn.boardservice.command.domain.comment.CommentLikeId

interface CommentLikeRepository {
    fun save(entity: CommentLike): CommentLike
    fun countByCommentId(commentId: Long): Long
    fun deleteById(id: CommentLikeId): Boolean
    fun findCommentIdByArticleIdAndUserId(articleId: Long, userId: Long): List<Long>
}
