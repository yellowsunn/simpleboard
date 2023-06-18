package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.command.domain.comment.Comment

data class CommentSavedDto(
    val commentId: Long,
    val nickName: String,
    val thumbnail: String?,
    val content: String,
    val imageUrl: String?,
    val parentCommentId: Long?,
    val baseCommentId: Long,
) {
    companion object {
        fun from(comment: Comment, userNickName: String, userThumbnail: String?) = CommentSavedDto(
            commentId = comment.id,
            nickName = userNickName,
            thumbnail = userThumbnail,
            content = comment.content ?: "",
            imageUrl = comment.imageUrl,
            parentCommentId = comment.parentCommentId,
            baseCommentId = comment.baseCommentId,
        )
    }
}
