package com.yellowsunn.boardservice.command.dto

data class ArticleSaveCommand(
    val userUUID: String,
    val title: String,
    val body: String,
)
