package com.yellowsunn.boardservice.dto

data class ArticleSaveCommand(
    val userId: Long,
    val title: String,
    val body: String,
)
