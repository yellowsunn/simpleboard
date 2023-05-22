package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ArticleEventKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${my-kafka.topic.article}") private val articleTopic: String,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun sendEvent(articleId: Long) {
        kafkaTemplate.send(articleTopic, articleId.toString())
            .whenComplete { _, e ->
                if (e != null) {
                    log.error("Send article event failed.", e)
                }
            }
    }
}
