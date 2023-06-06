package com.yellowsunn.boardservice.command.event.data.article

data class ArticleUndoLikeEvent(
    val articleId: Long,
    val userId: Long,
)
