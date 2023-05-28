package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.data.ProducerData
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

abstract class DefaultKafkaEventProducer {
    private val log = KotlinLogging.logger { }

    protected fun KafkaTemplate<String, ProducerData>.sendData(
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
