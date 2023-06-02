package com.yellowsunn.boardservice.command.event.data.comment

data class CommentLikeEvent(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
)
