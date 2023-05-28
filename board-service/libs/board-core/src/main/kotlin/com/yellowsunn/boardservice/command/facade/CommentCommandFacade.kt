package com.yellowsunn.boardservice.command.facade

import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.event.CommentEvent
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
        val user: User = userHttpClient.findUserByUserId(command.userId)
            ?: throw UserNotFoundException()

        return commentCommandService.saveComment(user, command).also {
            applicationEventPublisher.publishEvent(CommentEvent(it.commentId, command.articleId, command.userId))
        }
    }
}
