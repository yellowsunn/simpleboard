package com.yellowsunn.boardservice.command.event.producer.data

data class ArticleReactionDocumentSyncMessage(
    val articleId: Long,
    val userId: Long,
) : ProducerData
