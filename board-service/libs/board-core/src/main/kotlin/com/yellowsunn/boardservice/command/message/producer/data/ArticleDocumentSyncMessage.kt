package com.yellowsunn.boardservice.command.message.producer.data

data class ArticleDocumentSyncMessage(
    val articleId: Long,
) : ProducerData
