package com.yellowsunn.boardservice.command.event

class ArticleLikeEvent(
    val articleId: Long,
    val userId: Long,
    val isLiked: Boolean,
)
