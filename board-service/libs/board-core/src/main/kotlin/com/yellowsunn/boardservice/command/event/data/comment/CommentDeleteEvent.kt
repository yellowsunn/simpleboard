package com.yellowsunn.boardservice.command.event.data.comment

data class CommentDeleteEvent(
    val commentId: Long,
    val articleId: Long,
)
