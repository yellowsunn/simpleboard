package com.yellowsunn.boardservice.event.data.article

data class ArticleLikeEvent(
    val articleId: Long,
    val userId: Long,
)
