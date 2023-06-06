package com.yellowsunn.boardservice.command.dto

data class CommentReplySaveCommand(
    val userId: Long,
    val articleId: Long,
    val content: String,
    val imageUrl: String?,
    val parentCommentId: Long,
)
