package com.yellowsunn.boardservice.dto

data class ArticleSaveCommand(
    val userUUID: String,
    val title: String,
    val body: String,
)
