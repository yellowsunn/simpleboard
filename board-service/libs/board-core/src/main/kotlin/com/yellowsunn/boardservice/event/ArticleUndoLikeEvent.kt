package com.yellowsunn.boardservice.event

class ArticleUndoLikeEvent(
    val articleId: Long,
    val userId: Long,
) : Event
