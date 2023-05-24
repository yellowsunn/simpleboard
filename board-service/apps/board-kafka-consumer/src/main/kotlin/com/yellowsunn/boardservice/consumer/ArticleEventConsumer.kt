package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.event.ArticleCreateEvent
import com.yellowsunn.boardservice.event.ArticleLikeEvent
import com.yellowsunn.boardservice.event.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.event.ArticleUpdateEvent
import com.yellowsunn.boardservice.service.ArticleSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_CREATE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UNDO_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UPDATE_TOPIC
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class ArticleEventConsumer(
    private val articleSyncService: ArticleSyncService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private companion object {
        private const val ARTICLE_SYNC_GROUP = "article-sync-group"
    }

    @KafkaListener(
        topics = [ARTICLE_CREATE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun createArticleDocument(@Payload event: ArticleCreateEvent) {
        articleSyncService.syncArticle(event.articleId)
    }

    @KafkaListener(
        topics = [ARTICLE_UPDATE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun updateArticleDocument(@Payload event: ArticleUpdateEvent) {
        articleSyncService.syncArticle(event.articleId)
    }

    @KafkaListener(
        topics = [ARTICLE_LIKE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleLikeCount(@Payload event: ArticleLikeEvent) {
        val isUpdated = articleSyncService.syncArticleLike(event.articleId)
        if (isUpdated.not()) {
            logger.error("Failed to update article like. article id={}", event.articleId)
        }
    }

    @KafkaListener(
        topics = [ARTICLE_UNDO_LIKE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleUndoLikeCount(@Payload event: ArticleUndoLikeEvent) {
        val isUpdated = articleSyncService.syncArticleLike(event.articleId)
        if (isUpdated.not()) {
            logger.error("Failed to update article like. article id={}", event.articleId)
        }
    }
}
