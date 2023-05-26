package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.ArticleDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.ProducerData
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_DOCUMENT_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ArticleKafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, ProducerData>,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun syncArticleDocument(data: ArticleDocumentSyncData) {
        kafkaTemplate.sendData(ARTICLE_DOCUMENT_SYNC_TOPIC, data)
    }

    override fun syncArticleReactionDocument(data: ArticleReactionDocumentSyncData) {
        kafkaTemplate.sendData(ARTICLE_REACTION_DOCUMENT_SYNC_TOPIC, data)
    }

    private fun KafkaTemplate<String, ProducerData>.sendData(
        topic: String,
        data: ProducerData,
    ): CompletableFuture<SendResult<String, ProducerData>> {
        return this.send(topic, data)
            .whenComplete { _, e ->
                if (e != null) {
                    log.error("Failed to produce kafka data. topic={}, data={}", topic, data, e)
                }
            }
    }
}
