package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.event.producer.CommentEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.CommentDocumentSyncMessage
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import com.yellowsunn.common.constant.KafkaTopicConst.COMMENT_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CommentKafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    eventSendFailureRepository: EventSendFailureRepository,
) : CommentEventProducer,
    DefaultKafkaEventProducer(eventSendFailureRepository) {

    override fun syncCommentDocument(commentId: Long) {
        val data = CommentDocumentSyncMessage(commentId)
        kafkaTemplate.sendData(COMMENT_DOCUMENT_SYNC_TOPIC, data)
    }
}
