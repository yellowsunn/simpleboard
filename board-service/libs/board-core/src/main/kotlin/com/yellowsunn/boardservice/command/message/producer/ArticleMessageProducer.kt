package com.yellowsunn.boardservice.command.message.producer

interface ArticleMessageProducer {
    fun syncArticleDocument(articleId: Long)
    fun syncArticleReactionDocument(articleId: Long, userId: Long)
}
