package com.yellowsunn.boardservice.command.dto

data class ArticleUndoLikeCommand(
    val userUUID: String,
    val articleId: Long,
)
