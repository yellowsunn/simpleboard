package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.ArticleEvent
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ArticleEventListener(
    private val articleEventProducer: ArticleEventProducer,
) {
    @EventListener
    fun syncArticleDocument(event: ArticleEvent) {
        articleEventProducer.syncArticleDocument(ArticleDocumentSyncData(event.articleId))
    }
}
