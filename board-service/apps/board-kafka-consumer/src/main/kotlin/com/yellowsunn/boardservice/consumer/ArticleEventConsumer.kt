package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData
import com.yellowsunn.boardservice.service.ArticleSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_DOCUMENT_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC
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
        topics = [ARTICLE_DOCUMENT_SYNC_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleDocument(@Payload payload: ArticleDocumentSyncData) {
        articleSyncService.syncArticleDocument(payload.articleId)
    }

    @KafkaListener(
        topics = [ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC],
        groupId = ARTICLE_SYNC_GROUP,
    )
    fun syncArticleReactionDocument(@Payload payload: ArticleReactionDocumentSyncData) {
        articleSyncService.syncArticleReactionDocument(payload.articleId, payload.userId)
    }
}
