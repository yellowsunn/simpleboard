package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.dto.ArticleSaveRequestDto
import com.yellowsunn.boardservice.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.dto.ArticleUpdateRequestDto
import com.yellowsunn.boardservice.facade.ArticleCommandFacade
import com.yellowsunn.common.annotation.LoginUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleCommandController(
    private val articleCommandFacade: ArticleCommandFacade,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles")
    fun createArticle(
        @LoginUser userUUID: String,
        @Valid @RequestBody
        requestDto: ArticleSaveRequestDto,
    ): Long {
        val command = requestDto.toCommand(userUUID)
        return articleCommandFacade.saveArticle(command)
    }

    @PutMapping("/api/v2/articles/{articleId}")
    fun updateArticle(
        @LoginUser userUUID: String,
        @PathVariable articleId: Long,
        @Valid @RequestBody
        requestDto: ArticleUpdateRequestDto,
    ): Boolean {
        val command = requestDto.toCommand(userUUID, articleId)
        return articleCommandFacade.updateArticle(command)
    }

    @PutMapping("/api/v2/articles/{articleId}/like")
    fun likeArticle(
        @LoginUser userUUID: String,
        @PathVariable articleId: Long,
    ): Boolean {
        val command = ArticleLikeCommand(userUUID, articleId)
        return articleCommandFacade.likeArticle(command)
    }

    @DeleteMapping("/api/v2/articles/{articleId}/like")
    fun undoLikeArticle(
        @LoginUser userUUID: String,
        @PathVariable articleId: Long,
    ): Boolean {
        val command = ArticleUndoLikeCommand(userUUID, articleId)
        return articleCommandFacade.undoLikeArticle(command)
    }
}
