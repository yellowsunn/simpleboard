package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.ArticleSaveRequestDto
import com.yellowsunn.boardservice.facade.ArticleFacade
import com.yellowsunn.common.annotation.LoginUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    private val articleFacade: ArticleFacade,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles")
    fun createArticle(
        @LoginUser userUUID: String,
        @Valid @RequestBody
        requestDto: ArticleSaveRequestDto,
    ): String {
        val command = requestDto.toCommand(userUUID)
        return articleFacade.saveArticle(command)
    }
}
