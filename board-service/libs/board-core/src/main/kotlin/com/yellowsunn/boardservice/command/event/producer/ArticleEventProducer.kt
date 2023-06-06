package com.yellowsunn.boardservice.command.event.producer

interface ArticleEventProducer {
    fun syncArticleDocument(articleId: Long)
    fun syncArticleReactionDocument(articleId: Long, userId: Long)
}
