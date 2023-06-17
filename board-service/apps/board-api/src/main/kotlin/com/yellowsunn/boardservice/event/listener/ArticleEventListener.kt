package com.yellowsunn.boardservice.event.listener

import com.yellowsunn.boardservice.command.message.producer.ArticleMessageProducer
import com.yellowsunn.boardservice.command.message.producer.NotificationMessageProducer
import com.yellowsunn.boardservice.command.message.producer.data.notification.NotificationMessage
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.command.repository.RateLimitCacheRepository
import com.yellowsunn.boardservice.event.data.article.ArticleDeleteEvent
import com.yellowsunn.boardservice.event.data.article.ArticleLikeEvent
import com.yellowsunn.boardservice.event.data.article.ArticleSaveEvent
import com.yellowsunn.boardservice.event.data.article.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.event.data.article.ArticleUpdateEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class ArticleEventListener(
    private val articleMessageProducer: ArticleMessageProducer,
    private val notificationMessageProducer: NotificationMessageProducer,
    private val articleRepository: ArticleRepository,
    private val rateLimitCacheRepository: RateLimitCacheRepository,
) {
    @Async
    @EventListener
    fun saveArticle(event: ArticleSaveEvent) {
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun updateArticle(event: ArticleUpdateEvent) {
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun deleteArticle(event: ArticleDeleteEvent) {
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun likeArticle(event: ArticleLikeEvent) {
        articleMessageProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun notifyLikeArticle(event: ArticleLikeEvent) {
        val article = articleRepository.findById(event.articleId) ?: return
        if (article.userId == event.userId) {
            return
        }

        // 1분동안 한번의 요청만 전달
        val isAcquired: Boolean = rateLimitCacheRepository.acquireJustOne(
            "userId:${article.userId}:articleId:${article.id}",
            Duration.ofMinutes(1L),
        )
        if (isAcquired.not()) {
            return
        }

        val notificationMessage = NotificationMessage.buildArticleLikeMessage(
            articleId = article.id,
            userId = article.userId,
            content = article.title,
        )
        notificationMessageProducer.notify(notificationMessage)
    }

    @Async
    @EventListener
    fun undoLikeArticle(event: ArticleUndoLikeEvent) {
        articleMessageProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
        articleMessageProducer.syncArticleDocument(event.articleId)
    }
}