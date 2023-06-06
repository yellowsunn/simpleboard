package com.yellowsunn.boardservice.command.event.data.comment

data class CommentSaveEvent(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
    val content: String,
    val isReply: Boolean,
)
