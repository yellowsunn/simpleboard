package com.yellowsunn.boardservice.event

data class ArticleCreateEvent(
    val articleId: Long,
    val userId: Long,
) : Event
