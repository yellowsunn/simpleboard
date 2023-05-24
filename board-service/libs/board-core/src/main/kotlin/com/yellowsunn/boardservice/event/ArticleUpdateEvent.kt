package com.yellowsunn.boardservice.event

data class ArticleUpdateEvent(
    val articleId: Long,
    val userId: Long,
) : Event
