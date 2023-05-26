package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.ArticleLikeEvent
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ArticleLikeEventListener(
    private val articleEventProducer: ArticleEventProducer,
) {
    @EventListener
    fun syncArticleDocument(event: ArticleLikeEvent) {
        articleEventProducer.syncArticleDocument(
            ArticleDocumentSyncData(articleId = event.articleId),
        )
    }

    @EventListener
    fun syncArticleReactionDocument(event: ArticleLikeEvent) {
        articleEventProducer.syncArticleReactionDocument(
            ArticleReactionDocumentSyncData(
                articleId = event.articleId,
                userId = event.userId,
            ),
        )
    }
}

