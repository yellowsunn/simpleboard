package com.yellowsunn.boardservice.command.event.producer.data

data class ArticleDocumentSyncMessage(
    val articleId: Long,
) : ProducerData
