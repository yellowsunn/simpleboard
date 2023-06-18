package com.yellowsunn.boardservice.event.data.article

data class ArticleUndoLikeEvent(
    val articleId: Long,
    val userId: Long,
)
