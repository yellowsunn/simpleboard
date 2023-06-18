package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.message.producer.ArticleMessageProducer
import com.yellowsunn.boardservice.command.message.producer.data.ArticleDocumentSyncMessage
import com.yellowsunn.boardservice.command.message.producer.data.ArticleReactionDocumentSyncMessage
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_DOCUMENT_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ArticleKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : ArticleMessageProducer,
    DefaultKafkaEventProducer() {

    override fun syncArticleDocument(articleId: Long) {
        val data = ArticleDocumentSyncMessage(articleId)
        kafkaTemplate.sendData(ARTICLE_DOCUMENT_SYNC_TOPIC, data)
    }

    override fun syncArticleReactionDocument(articleId: Long, userId: Long) {
        val data = ArticleReactionDocumentSyncMessage(articleId = articleId, userId = userId)
        kafkaTemplate.sendData(ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC, data)
    }
}
