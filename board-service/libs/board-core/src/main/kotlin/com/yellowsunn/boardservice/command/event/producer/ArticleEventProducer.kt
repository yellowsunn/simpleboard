package com.yellowsunn.boardservice.command.event.producer

import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData

interface ArticleEventProducer {
    fun syncArticleDocument(data: ArticleDocumentSyncData)
    fun syncArticleReactionDocument(data: ArticleReactionDocumentSyncData)
}
