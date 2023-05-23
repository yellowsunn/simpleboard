package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.dto.ArticleSaveRequestDto
import com.yellowsunn.boardservice.facade.ArticleFacade
import com.yellowsunn.boardservice.service.ArticleQueryService
import com.yellowsunn.common.annotation.LoginUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    private val articleFacade: ArticleFacade,
    private val articleQueryService: ArticleQueryService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles")
    fun createArticle(
        @LoginUser userUUID: String,
        @Valid @RequestBody
        requestDto: ArticleSaveRequestDto,
    ): Long {
        val command = requestDto.toCommand(userUUID)
        return articleFacade.saveArticle(command)
    }

    @GetMapping("/api/v2/articles/{documentId}")
    fun getArticle(@PathVariable documentId: String): ArticleDocumentDto {
        return articleQueryService.findArticleByDocumentId(documentId)
    }

    @GetMapping("/api/v2/articles")
    fun getArticles(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ArticleDocumentPageDto {
        return articleQueryService.findArticles(page, size)
    }
}
