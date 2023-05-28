package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.query.dto.CommentDocumentPageDto
import com.yellowsunn.boardservice.query.service.CommentQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentQueryController(
    private val commentQueryService: CommentQueryService,
) {
    @GetMapping("/api/v2/articles/{articleId}/comments")
    fun getComments(
        @PathVariable articleId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): CommentDocumentPageDto {
        return commentQueryService.findComments(articleId, page, size)
    }

    @GetMapping("/api/v2/comments/{commentId}/page")
    fun getCommentPage(
        @PathVariable commentId: Long,
        @RequestParam(defaultValue = "10") size: Int,
    ): Long? {
        return commentQueryService.findCommentPage(commentId, size)
    }
}
