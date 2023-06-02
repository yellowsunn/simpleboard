package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.data.article.ArticleDeleteEvent
import com.yellowsunn.boardservice.command.event.data.article.ArticleLikeEvent
import com.yellowsunn.boardservice.command.event.data.article.ArticleSaveEvent
import com.yellowsunn.boardservice.command.event.data.article.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.command.event.data.article.ArticleUpdateEvent
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ArticleEventListener(
    private val articleEventProducer: ArticleEventProducer,
) {
    @EventListener
    fun saveArticle(event: ArticleSaveEvent) {
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun updateArticle(event: ArticleUpdateEvent) {
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun deleteArticle(event: ArticleDeleteEvent) {
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun likeArticle(event: ArticleLikeEvent) {
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun undoLikeArticle(event: ArticleUndoLikeEvent) {
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
        articleEventProducer.syncArticleDocument(event.articleId)
    }
}
