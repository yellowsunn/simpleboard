package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.event.ArticleLikeSyncEvent
import com.yellowsunn.boardservice.event.ArticleSyncEvent
import com.yellowsunn.boardservice.service.ArticleSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_SYNC_TOPIC
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
        topics = [ARTICLE_SYNC_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleDocument(@Payload event: ArticleSyncEvent) {
        articleSyncService.syncArticle(event.articleId)
    }

    @KafkaListener(
        topics = [ARTICLE_LIKE_SYNC_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleLikeCount(@Payload event: ArticleLikeSyncEvent) {
        val isUpdated = articleSyncService.syncArticleLike(event.articleId)
        if (isUpdated.not()) {
            logger.error("Failed to sync article like. article id={}", event.articleId)
        }
    }
}
