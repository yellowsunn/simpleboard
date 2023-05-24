package com.yellowsunn.boardservice.event

data class ArticleSyncEvent(
    val articleId: Long,
) : Event
