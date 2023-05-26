package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.command.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.command.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.command.facade.ArticleCommandFacade
import com.yellowsunn.boardservice.dto.ArticleSaveRequestDto
import com.yellowsunn.boardservice.dto.ArticleUpdateRequestDto
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
        @LoginUser userId: Long,
        @Valid @RequestBody
        requestDto: ArticleSaveRequestDto,
    ): Long {
        val command = requestDto.toCommand(userId)
        return articleCommandFacade.saveArticle(command)
    }

    @PutMapping("/api/v2/articles/{articleId}")
    fun updateArticle(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @Valid @RequestBody
        requestDto: ArticleUpdateRequestDto,
    ): Boolean {
        val command = requestDto.toCommand(userId, articleId)
        return articleCommandFacade.updateArticle(command)
    }

    @DeleteMapping("/api/v2/articles/{articleId}")
    fun deleteArticle(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
    ): Boolean {
        return articleCommandFacade.deleteArticle(userId, articleId)
    }

    @PutMapping("/api/v2/articles/{articleId}/like")
    fun likeArticle(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
    ): Boolean {
        val command = ArticleLikeCommand(userId, articleId)
        return articleCommandFacade.likeArticle(command)
    }

    @DeleteMapping("/api/v2/articles/{articleId}/like")
    fun undoLikeArticle(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
    ): Boolean {
        val command = ArticleUndoLikeCommand(userId, articleId)
        return articleCommandFacade.undoLikeArticle(command)
    }
}
