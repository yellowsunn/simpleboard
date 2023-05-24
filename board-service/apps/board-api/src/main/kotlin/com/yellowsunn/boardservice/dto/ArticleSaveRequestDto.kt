package com.yellowsunn.boardservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ArticleSaveRequestDto(
    @NotBlank
    @Size(min = 1, max = 200)
    val title: String,

    @NotBlank
    @Size(max = 10_000)
    val body: String,
) {
    fun toCommand(userUUID: String) = ArticleSaveCommand(
        userUUID = userUUID,
        title = this.title,
        body = this.body,
    )
}