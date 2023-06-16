package com.yellowsunn.boardservice.dto

data class ArticleUndoLikeCommand(
    val userId: Long,
    val articleId: Long,
)
