package com.yellowsunn.boardservice.command.event.producer.data

data class CommentDocumentSyncMessage(
    val commentId: Long,
) : ProducerData
