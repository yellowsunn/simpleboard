package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.service.ArticleQueryService
import com.yellowsunn.boardservice.service.ArticleQuerySyncService
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
    private val articleQueryService: ArticleQueryService,
    private val articleQuerySyncService: ArticleQuerySyncService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [ARTICLE_TOPIC],
        groupId = "article-sync-group",
    )
    fun syncArticleDocument(@Payload articleId: Long) {
        val isSaved = articleQueryService.sync(articleId)
        if (isSaved.not()) {
            logger.error("Failed to sync article. article id={}", articleId)
        }
    }

    @KafkaListener(
        topics = [ARTICLE_LIKE_TOPIC, ARTICLE_UNDO_LIKE_TOPIC],
        groupId = "article-sync-group",
    )
    fun syncArticleLikeCount(@Payload articleId: Long) {
        val isUpdated = articleQuerySyncService.syncArticleLike(articleId)
        if (isUpdated.not()) {
            logger.error("Failed to update article like. article id={}", articleId)
        }
    }
}
