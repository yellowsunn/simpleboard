package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.CommentEvent
import com.yellowsunn.boardservice.command.event.producer.CommentEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.CommentDocumentSyncData
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CommentEventListener(
    private val commentEventProducer: CommentEventProducer,
) {
    @EventListener
    fun syncCommentDocument(event: CommentEvent) {
        commentEventProducer.syncCommentDocument(CommentDocumentSyncData(event.commentId))
    }
}
