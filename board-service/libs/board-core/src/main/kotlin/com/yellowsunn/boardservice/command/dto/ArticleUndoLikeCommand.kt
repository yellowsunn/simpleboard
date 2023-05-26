package com.yellowsunn.boardservice.command.dto

data class ArticleUndoLikeCommand(
    val userId: Long,
    val articleId: Long,
)
