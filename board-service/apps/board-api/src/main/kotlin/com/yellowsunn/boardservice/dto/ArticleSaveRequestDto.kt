package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.command.dto.ArticleSaveCommand
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ArticleSaveRequestDto(
    @field:NotBlank
    @field:Size(min = 1, max = 200)
    val title: String,

    @field:NotBlank
    @field:Size(max = 10_000)
    val body: String,
) {
    fun toCommand(userId: Long) = ArticleSaveCommand(
        userId = userId,
        title = this.title,
        body = this.body,
    )
}
