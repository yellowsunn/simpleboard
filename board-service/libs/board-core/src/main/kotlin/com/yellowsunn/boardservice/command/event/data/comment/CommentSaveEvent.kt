package com.yellowsunn.boardservice.command.event.data.comment

data class CommentSaveEvent(
    val commentId: Long,
    val articleId: Long,
)
