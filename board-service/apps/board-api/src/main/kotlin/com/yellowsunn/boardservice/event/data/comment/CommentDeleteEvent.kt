package com.yellowsunn.boardservice.event.data.comment

data class CommentDeleteEvent(
    val commentId: Long,
    val articleId: Long,
)
