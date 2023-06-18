package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.CommentDocumentPageDto
import com.yellowsunn.boardservice.dto.UserCommentDocumentPageDto
import com.yellowsunn.boardservice.service.CommentQueryService
import com.yellowsunn.common.annotation.LoginUser
import com.yellowsunn.common.response.ResultResponse
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
    ): ResultResponse<CommentDocumentPageDto> {
        return ResultResponse.ok(
            commentQueryService.findComments(articleId, page, size),
        )
    }

    @GetMapping("/api/v2/comments/me")
    fun getMyComments(
        @LoginUser userId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResultResponse<UserCommentDocumentPageDto> {
        return ResultResponse.ok(
            commentQueryService.findUserComments(userId, page, size),
        )
    }

    @GetMapping("/api/v2/comments/{commentId}/page")
    fun getCommentPage(
        @PathVariable commentId: Long,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResultResponse<Long?> {
        return ResultResponse.ok(
            commentQueryService.findCommentPage(commentId, size),
        )
    }
}
