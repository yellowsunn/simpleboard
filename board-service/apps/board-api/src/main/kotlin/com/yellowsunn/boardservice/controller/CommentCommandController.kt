package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.facade.CommentCommandFacade
import com.yellowsunn.boardservice.dto.CommentSaveRequestDto
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
    ): CommentSavedDto {
        val command = requestDto.toCommand(userId, articleId)
        return commentCommandFacade.saveComment(command)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/articles/{articleId}/comments/{commentId}")
    fun createReplyComment(
        @LoginUser userId: Long,
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody
        requestDto: CommentSaveRequestDto,
    ): CommentSavedDto {
        val command = requestDto.toCommand(userId, articleId, commentId)
        return commentCommandFacade.saveComment(command)
    }

    @PutMapping("/api/v2/comments/{commentId}/like")
    fun likeComment(
        @LoginUser userId: Long,
        @PathVariable commentId: Long,
    ): Boolean {
        return commentCommandFacade.likeComment(userId, commentId)
    }

    @DeleteMapping("/api/v2/comments/{commentId}/like")
    fun undoLikeComment(
        @LoginUser userId: Long,
        @PathVariable commentId: Long,
    ): Boolean {
        return commentCommandFacade.undoLikeComment(userId, commentId)
    }
}
