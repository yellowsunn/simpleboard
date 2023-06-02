package com.yellowsunn.boardservice.command.facade

import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.event.data.comment.CommentDeleteEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentLikeEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentSaveEvent
import com.yellowsunn.boardservice.command.service.CommentCommandService
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.common.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CommentCommandFacade(
    private val userHttpClient: UserHttpClient,
    private val commentCommandService: CommentCommandService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun saveComment(command: CommentSaveCommand): CommentSavedDto {
        val user: User = getUserById(command.userId)

        val commentSavedDto: CommentSavedDto = commentCommandService.saveComment(command, user)

        applicationEventPublisher.publishEvent(CommentSaveEvent(commentSavedDto.commentId, command.articleId))
        return commentSavedDto
    }

    fun deleteComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isDeleted: Boolean = commentCommandService.deleteComment(commentId, articleId)

        applicationEventPublisher.publishEvent(CommentDeleteEvent(commentId, articleId))
        return isDeleted
    }

    fun likeComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isUpdated: Boolean = commentCommandService.likeComment(commentId, articleId, userId)

        applicationEventPublisher.publishEvent(
            CommentLikeEvent(commentId, articleId, userId),
        )
        return isUpdated
    }

    fun undoLikeComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isDeleted: Boolean = commentCommandService.undoLikeComment(commentId, articleId, userId)

        applicationEventPublisher.publishEvent(
            CommentLikeEvent(commentId, articleId, userId),
        )
        return isDeleted
    }

    private fun getUserById(userId: Long): User {
        return userHttpClient.findUserByUserId(userId)
            ?: throw UserNotFoundException()
    }

    private fun checkValidUser(userId: Long) {
        getUserById(userId)
    }
}
