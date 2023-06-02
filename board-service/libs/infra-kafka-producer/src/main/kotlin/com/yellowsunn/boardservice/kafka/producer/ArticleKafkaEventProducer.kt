package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ProducerData
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_DOCUMENT_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ArticleKafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, ProducerData>,
) : ArticleEventProducer,
    DefaultKafkaEventProducer() {

    override fun syncArticleDocument(data: ArticleDocumentSyncData) {
        kafkaTemplate.sendData(ARTICLE_DOCUMENT_SYNC_TOPIC, data)
    }

    override fun syncArticleDocument(articleId: Long) {
        val data = ArticleDocumentSyncData(articleId)
        kafkaTemplate.sendData(ARTICLE_DOCUMENT_SYNC_TOPIC, data)
    }

    override fun syncArticleReactionDocument(data: ArticleReactionDocumentSyncData) {
        kafkaTemplate.sendData(ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC, data)
    }

    override fun syncArticleReactionDocument(articleId: Long, userId: Long) {
        val data = ArticleReactionDocumentSyncData(articleId = articleId, userId = userId)
        kafkaTemplate.sendData(ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC, data)
    }
}
