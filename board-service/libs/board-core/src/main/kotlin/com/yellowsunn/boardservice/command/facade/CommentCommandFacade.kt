package com.yellowsunn.boardservice.command.facade

import com.yellowsunn.boardservice.command.dto.CommentLikeDto
import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.CommentEventProducer
import com.yellowsunn.boardservice.command.event.producer.data.ArticleReactionDocumentSyncData
import com.yellowsunn.boardservice.command.event.producer.data.CommentDocumentSyncData
import com.yellowsunn.boardservice.command.service.CommentCommandService
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.common.exception.UserNotFoundException
import org.springframework.stereotype.Component

@Component
class CommentCommandFacade(
    private val userHttpClient: UserHttpClient,
    private val commentCommandService: CommentCommandService,
    private val commentEventProducer: CommentEventProducer,
    private val articleEventProducer: ArticleEventProducer,
) {
    fun saveComment(command: CommentSaveCommand): CommentSavedDto {
        val user: User = getUserById(command.userId)

        return commentCommandService.saveComment(user, command).also {
            commentEventProducer.syncCommentDocument(CommentDocumentSyncData(it.commentId))
        }
    }

    fun likeComment(userId: Long, commentId: Long): Boolean {
        val user: User = getUserById(userId)

        val commentLikeDto: CommentLikeDto? = commentCommandService.likeComment(user.userId, commentId)?.also {
            commentEventProducer.syncCommentDocument(CommentDocumentSyncData(it.commentId))
            articleEventProducer.syncArticleReactionDocument(ArticleReactionDocumentSyncData(it.articleId, userId))
        }
        return commentLikeDto != null
    }

    fun undoLikeComment(userId: Long, commentId: Long): Boolean {
        val user: User = getUserById(userId)

        val commentLikeDto: CommentLikeDto? = commentCommandService.undoLikeComment(user.userId, commentId)?.also {
            commentEventProducer.syncCommentDocument(CommentDocumentSyncData(it.commentId))
            articleEventProducer.syncArticleReactionDocument(ArticleReactionDocumentSyncData(it.articleId, userId))
        }
        return commentLikeDto != null
    }

    private fun getUserById(userId: Long): User {
        return userHttpClient.findUserByUserId(userId)
            ?: throw UserNotFoundException()
    }
}
