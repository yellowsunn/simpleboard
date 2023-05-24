package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.event.ArticleCreateEvent
import com.yellowsunn.boardservice.event.ArticleLikeEvent
import com.yellowsunn.boardservice.event.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.event.ArticleUpdateEvent
import com.yellowsunn.boardservice.event.Event
import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_CREATE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UNDO_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UPDATE_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ArticleKafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Event>,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun saveArticleEvent(event: ArticleCreateEvent) {
        kafkaTemplate.sendData(ARTICLE_CREATE_TOPIC, event)
    }

    override fun updateArticleEvent(event: ArticleUpdateEvent) {
        kafkaTemplate.sendData(ARTICLE_UPDATE_TOPIC, event)
    }

    override fun updateArticleLikeEvent(event: ArticleLikeEvent) {
        kafkaTemplate.sendData(ARTICLE_LIKE_TOPIC, event)
    }

    override fun updateArticleUndoLikeEvent(event: ArticleUndoLikeEvent) {
        kafkaTemplate.sendData(ARTICLE_UNDO_LIKE_TOPIC, event)
    }

    private fun KafkaTemplate<String, Event>.sendData(
        topic: String,
        data: Event,
    ): CompletableFuture<SendResult<String, Event>> {
        return this.send(topic, data)
            .whenComplete { _, e ->
                if (e != null) {
                    log.error("Failed to produce kafka data. topic={}, data={}", topic, data, e)
                }
            }
    }
}
