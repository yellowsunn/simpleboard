package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.common.constant.KafkaTopicConst.ARTICLE_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ArticleEventKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : ArticleEventProducer {
    private val log = KotlinLogging.logger { }

    override fun sendEvent(articleId: Long) {
        kafkaTemplate.send(ARTICLE_TOPIC, articleId.toString())
            .whenComplete { _, e ->
                if (e != null) {
                    log.error("Send article event failed.", e)
                }
            }
    }
}
