package com.yellowsunn.boardservice.command.event.producer

import com.yellowsunn.boardservice.command.event.producer.data.CommentDocumentSyncData

interface CommentEventProducer {
    fun syncCommentDocument(data: CommentDocumentSyncData)
}
