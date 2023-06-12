package com.yellowsunn.boardservice.consumer

import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncMessage
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncMessage
import com.yellowsunn.boardservice.constant.SYNC_GROUP
import com.yellowsunn.boardservice.service.ArticleSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_DOCUMENT_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class ArticleEventConsumer(
    private val articleSyncService: ArticleSyncService,
) {
    @KafkaListener(
        topics = [ARTICLE_DOCUMENT_SYNC_TOPIC],
        groupId = SYNC_GROUP,
    )
    fun syncArticleDocument(@Payload payload: ArticleDocumentSyncMessage) {
        articleSyncService.syncArticleDocument(payload.articleId)
    }

    @KafkaListener(
        topics = [ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC],
        groupId = SYNC_GROUP,
    )
    fun syncArticleReactionDocument(@Payload payload: ArticleReactionDocumentSyncMessage) {
        articleSyncService.syncArticleReactionDocument(payload.articleId, payload.userId)
    }
}
