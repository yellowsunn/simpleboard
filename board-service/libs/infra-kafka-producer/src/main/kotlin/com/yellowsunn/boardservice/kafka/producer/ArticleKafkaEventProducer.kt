package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.event.ArticleLikeSyncEvent
import com.yellowsunn.boardservice.event.ArticleSyncEvent
import com.yellowsunn.boardservice.event.Event
import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_SYNC_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_SYNC_TOPIC
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

@Component
class ArticleKafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Event>,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun syncArticleEvent(event: ArticleSyncEvent) {
        kafkaTemplate.sendData(ARTICLE_SYNC_TOPIC, event)
    }

    override fun syncArticleLikeEvent(event: ArticleLikeSyncEvent) {
        kafkaTemplate.sendData(ARTICLE_LIKE_SYNC_TOPIC, event)
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
