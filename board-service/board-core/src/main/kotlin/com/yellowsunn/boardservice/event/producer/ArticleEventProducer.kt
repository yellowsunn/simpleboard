package com.yellowsunn.boardservice.event.producer

interface ArticleEventProducer {
    fun sendEvent(articleId: Long)
}
