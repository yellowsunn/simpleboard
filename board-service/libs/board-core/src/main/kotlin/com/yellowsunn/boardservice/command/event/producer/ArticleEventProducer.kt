package com.yellowsunn.boardservice.command.event.producer

import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData

interface ArticleEventProducer {
    fun syncArticleDocument(data: ArticleDocumentSyncData)
    fun syncArticleDocument(articleId: Long)
    fun syncArticleReactionDocument(data: ArticleReactionDocumentSyncData)
    fun syncArticleReactionDocument(articleId: Long, userId: Long)
}
