package com.yellowsunn.boardservice.command.message.producer.data

data class CommentDocumentSyncMessage(
    val commentId: Long,
) : ProducerData
