package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_LIKE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_TOPIC
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_UNDO_LIKE_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ArticleEventKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun saveArticleEvent(articleId: Long) {
        kafkaTemplate.send(ARTICLE_TOPIC, articleId.toString())
            .logging(ARTICLE_TOPIC)
    }

    override fun updateArticleLikeEvent(articleId: Long) {
        kafkaTemplate.send(ARTICLE_LIKE_TOPIC, articleId.toString())
            .logging(ARTICLE_LIKE_TOPIC)
    }

    override fun updateArticleUndoLikeEvent(articleId: Long) {
        kafkaTemplate.send(ARTICLE_UNDO_LIKE_TOPIC, articleId.toString())
            .logging(ARTICLE_UNDO_LIKE_TOPIC)
    }

    private fun <T> CompletableFuture<T>.logging(topic: String): CompletableFuture<T> {
        return this.whenComplete { _, e ->
            if (e != null) {
                log.error("Failed to produce kafka data. topic={}", topic, e)
            }
        }
    }
}
