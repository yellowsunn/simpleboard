package com.yellowsunn.boardservice.dto

data class ArticleUndoLikeCommand(
    val userUUID: String,
    val articleId: Long,
)
