package com.yellowsunn.boardservice.command.message.producer.data

data class ArticleReactionDocumentSyncMessage(
    val articleId: Long,
    val userId: Long,
) : ProducerData
