package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.service.ArticleQueryService
import com.yellowsunn.common.annotation.LoginUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleQueryController(
    private val articleQueryService: ArticleQueryService,
) {
    @GetMapping("/api/v2/articles")
    fun getArticles(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ArticleDocumentPageDto {
        return articleQueryService.findArticles(page, size)
    }

    @GetMapping("/api/v2/articles/{documentId}")
    fun getArticle(@PathVariable documentId: String): ArticleDocumentDto {
        return articleQueryService.findArticleByDocumentId(documentId)
    }

    @GetMapping("/api/v2/articles/{documentId}/reaction")
    fun getArticleReaction(
        @LoginUser(required = false) userUUID: String?,
        @PathVariable documentId: String,
    ) {
        println()
        return
    }
}
