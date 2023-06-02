package com.yellowsunn.boardservice.command.event.data.article

data class ArticleLikeEvent(
    val articleId: Long,
    val userId: Long,
)
