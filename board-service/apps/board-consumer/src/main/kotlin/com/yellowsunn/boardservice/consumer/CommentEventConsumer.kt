package com.yellowsunn.boardservice.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.boardservice.command.event.producer.data.CommentDocumentSyncMessage
import com.yellowsunn.boardservice.constant.SYNC_GROUP
import com.yellowsunn.boardservice.service.CommentSyncService
import com.yellowsunn.common.constant.KafkaTopicConst.COMMENT_DOCUMENT_SYNC_TOPIC
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CommentEventConsumer(
    private val commentSyncService: CommentSyncService,
) {
    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(
        topics = [COMMENT_DOCUMENT_SYNC_TOPIC],
        groupId = SYNC_GROUP,
    )
    fun syncCommentDocument(@Payload payload: String) {
        val message = objectMapper.readValue(payload, CommentDocumentSyncMessage::class.java)
        commentSyncService.syncCommentDocument(message.commentId)
    }
}
