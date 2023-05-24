package com.yellowsunn.boardservice.event

class ArticleLikeEvent(
    val articleId: Long,
    val userId: Long,
) : Event
