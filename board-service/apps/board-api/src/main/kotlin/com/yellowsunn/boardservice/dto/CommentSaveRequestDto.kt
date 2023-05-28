package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import jakarta.validation.constraints.Size

data class CommentSaveRequestDto(
    @field:Size(max = 150)
    val content: String?,

    @field:Size(max = 500)
    val imageUrl: String?,
) {
    fun toCommand(userId: Long, articleId: Long, commentId: Long? = null): CommentSaveCommand {
        checkEmptyRequest(content, imageUrl)

        return CommentSaveCommand(
            userId = userId,
            articleId = articleId,
            content = content ?: "",
            imageUrl = imageUrl,
            parentCommentId = commentId,
        )
    }

    private fun checkEmptyRequest(content: String?, imageUrl: String?) {
        if (content.isNullOrBlank() && imageUrl.isNullOrBlank()) {
            throw IllegalArgumentException("빈 요청은 허용되지 않습니다.")
        }
    }
}
