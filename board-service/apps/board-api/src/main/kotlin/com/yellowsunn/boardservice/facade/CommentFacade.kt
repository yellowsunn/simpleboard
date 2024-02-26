package com.yellowsunn.boardservice.facade

import com.yellowsunn.boardservice.dto.CommentSaveCommand
import com.yellowsunn.boardservice.dto.CommentSavedDto
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.event.data.comment.CommentDeleteEvent
import com.yellowsunn.boardservice.event.data.comment.CommentLikeEvent
import com.yellowsunn.boardservice.event.data.comment.CommentSaveEvent
import com.yellowsunn.boardservice.service.CommentService
import com.yellowsunn.common.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CommentFacade(
    private val userHttpClient: UserHttpClient,
    private val commentService: CommentService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun saveComment(command: CommentSaveCommand): CommentSavedDto {
        val user: User = getUserById(command.userId)

        val commentSavedDto: CommentSavedDto = commentService.saveComment(command, user)

        applicationEventPublisher.publishEvent(
            CommentSaveEvent(
                commentId = commentSavedDto.commentId,
                articleId = command.articleId,
                userId = user.userId,
                content = commentSavedDto.content,
                isReply = commentSavedDto.parentCommentId != null,
            ),
        )
        return commentSavedDto
    }

    fun deleteComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isDeleted: Boolean = commentService.deleteComment(commentId, articleId)

        applicationEventPublisher.publishEvent(CommentDeleteEvent(commentId, articleId))
        return isDeleted
    }

    fun likeComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isUpdated: Boolean = commentService.likeComment(commentId, articleId, userId)

        applicationEventPublisher.publishEvent(
            CommentLikeEvent(commentId, articleId, userId),
        )
        return isUpdated
    }

    fun undoLikeComment(commentId: Long, articleId: Long, userId: Long): Boolean {
        checkValidUser(userId)

        val isDeleted: Boolean = commentService.undoLikeComment(commentId, articleId, userId)

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
