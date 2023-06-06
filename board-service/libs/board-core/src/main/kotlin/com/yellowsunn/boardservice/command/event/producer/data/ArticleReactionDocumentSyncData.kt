package com.yellowsunn.boardservice.command.event.producer.data

data class ArticleReactionDocumentSyncData(
    val articleId: Long,
    val userId: Long,
) : ProducerData
