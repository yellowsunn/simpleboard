package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.query.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.query.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.query.dto.ArticleReactionDocumentDto
import com.yellowsunn.boardservice.query.dto.UserArticleDocumentPageDto
import com.yellowsunn.boardservice.query.service.ArticleQueryService
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

    @GetMapping("/api/v2/articles/me")
    fun getMyArticles(
        @LoginUser userId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): UserArticleDocumentPageDto {
        return articleQueryService.findUserArticles(userId, page, size)
    }

    @GetMapping("/api/v2/articles/{articleId}")
    fun getArticle(@PathVariable articleId: Long): ArticleDocumentDto {
        return articleQueryService.findArticleById(articleId)
    }

    @GetMapping("/api/v2/articles/{articleId}/reaction")
    fun getArticleReaction(
        @PathVariable articleId: Long,
        @LoginUser(required = false) userId: Long?,
    ): ArticleReactionDocumentDto? {
        return if (userId != null) {
            articleQueryService.findReactionByArticleId(articleId, userId)
        } else {
            null
        }
    }
}
