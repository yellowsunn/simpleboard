package com.yellowsunn.boardservice.command.repository

interface CommentLikeRepository {
    fun countByCommentId(commentId: Long): Long
}
