package com.yellowsunn.boardservice.command.event.producer

interface CommentEventProducer {
    fun syncCommentDocument(commentId: Long)
}
