package com.yellowsunn.boardservice.command.dto

data class ArticleSaveCommand(
    val userId: Long,
    val title: String,
    val body: String,
)
