package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.service.ArticleSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UNDO_LIKE_TOPIC
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
        topics = [ARTICLE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleDocument(@Payload articleId: Long) {
        val isSaved = articleSyncService.syncArticle(articleId)
        if (isSaved.not()) {
            logger.error("Failed to sync article. article id={}", articleId)
        }
    }

    @KafkaListener(
        topics = [ARTICLE_LIKE_TOPIC, ARTICLE_UNDO_LIKE_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleLikeCount(@Payload articleId: Long) {
        val isUpdated = articleSyncService.syncArticleLike(articleId)
        if (isUpdated.not()) {
            logger.error("Failed to update article like. article id={}", articleId)
        }
    }
}
