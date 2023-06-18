package com.yellowsunn.boardservice.command.message.producer

interface CommentMessageProducer {
    fun syncCommentDocument(commentId: Long)
}
