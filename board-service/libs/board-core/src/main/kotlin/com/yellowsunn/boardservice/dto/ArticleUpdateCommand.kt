package com.yellowsunn.boardservice.dto

data class ArticleUpdateCommand(
    val userUUID: String,
    val articleId: Long,
    val title: String,
    val body: String,
)
