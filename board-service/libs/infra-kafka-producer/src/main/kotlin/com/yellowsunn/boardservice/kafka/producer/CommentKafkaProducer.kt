package com.yellowsunn.boardservice.kafka.producer

import com.yellowsunn.boardservice.command.message.producer.CommentMessageProducer
import com.yellowsunn.boardservice.command.message.producer.data.CommentDocumentSyncMessage
import com.yellowsunn.common.constant.KafkaTopicConst.COMMENT_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CommentKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : CommentMessageProducer,
    DefaultKafkaEventProducer() {

    override fun syncCommentDocument(commentId: Long) {
        val data = CommentDocumentSyncMessage(commentId)
        kafkaTemplate.sendData(COMMENT_DOCUMENT_SYNC_TOPIC, data)
    }
}
