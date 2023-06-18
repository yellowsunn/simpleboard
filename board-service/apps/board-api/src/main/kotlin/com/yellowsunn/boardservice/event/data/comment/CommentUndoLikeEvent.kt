package com.yellowsunn.boardservice.event.data.comment

data class CommentUndoLikeEvent(
    val commentId: Long,
    val articleId: Long,
    val userId: Long,
)
