package com.yellowsunn.boardservice.event

data class ArticleLikeSyncEvent(
    val articleId: Long,
) : Event
