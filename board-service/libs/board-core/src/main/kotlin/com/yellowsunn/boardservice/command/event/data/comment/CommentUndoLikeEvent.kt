package com.yellowsunn.boardservice.command.event.data.comment

data class CommentUndoLikeEvent(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
)
