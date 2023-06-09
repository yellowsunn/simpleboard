package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.facade.CommentCommandFacade
import com.yellowsunn.boardservice.dto.CommentSaveRequestDto
import com.yellowsunn.common.annotation.LoginUser
import com.yellowsunn.common.response.ResultResponse
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
class CommentCommandController(
    private val commentCommandFacade: CommentCommandFacade,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles/{articleId}/comments")
    fun createComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @Valid @RequestBody
        requestDto: CommentSaveRequestDto,
    ): ResultResponse<CommentSavedDto> {
        val command = requestDto.toCommand(userId, articleId)
        return ResultResponse.ok(
            commentCommandFacade.saveComment(command),
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles/{articleId}/comments/{commentId}")
    fun createReplyComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody
        requestDto: CommentSaveRequestDto,
    ): ResultResponse<CommentSavedDto> {
        val command = requestDto.toCommand(userId, articleId, commentId)
        return ResultResponse.ok(
            commentCommandFacade.saveComment(command),
        )
    }

    @DeleteMapping("/api/v2/articles/{articleId}/comments/{commentId}")
    fun deleteComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
    ): ResultResponse<Boolean> {
        return ResultResponse.ok(
            commentCommandFacade.deleteComment(commentId, articleId, userId),
        )
    }

    @PutMapping("/api/v2/articles/{articleId}/comments/{commentId}/like")
    fun likeComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
    ): ResultResponse<Boolean> {
        return ResultResponse.ok(
            commentCommandFacade.likeComment(commentId, articleId, userId),
        )
    }

    @DeleteMapping("/api/v2/articles/{articleId}/comments/{commentId}/like")
    fun undoLikeComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
    ): ResultResponse<Boolean> {
        return ResultResponse.ok(
            commentCommandFacade.undoLikeComment(commentId, articleId, userId),
        )
    }
}
