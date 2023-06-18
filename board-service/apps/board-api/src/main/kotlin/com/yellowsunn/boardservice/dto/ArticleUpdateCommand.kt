package com.yellowsunn.boardservice.dto

data class ArticleUpdateCommand(
    val userId: Long,
    val articleId: Long,
    val title: String,
    val body: String,
)
