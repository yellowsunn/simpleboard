package com.yellowsunn.boardservice.command.event

data class CommentEvent(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
)
