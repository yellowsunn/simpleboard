package com.yellowsunn.boardservice.command.facade

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.command.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.command.dto.ArticleSaveCommand
import com.yellowsunn.boardservice.command.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.command.dto.ArticleUpdateCommand
import com.yellowsunn.boardservice.command.event.ArticleEvent
import com.yellowsunn.boardservice.command.event.ArticleLikeEvent
import com.yellowsunn.boardservice.command.service.ArticleCommandService
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.common.exception.UserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ArticleCommandFacade(
    private val userHttpClient: UserHttpClient,
    private val articleCommandService: ArticleCommandService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun saveArticle(command: ArticleSaveCommand): Long {
        val user: User = getUserById(command.userId)

        val article: Article = articleCommandService.saveArticle(user.userId, command.title, command.body)

        applicationEventPublisher.publishEvent(ArticleEvent(article.id, user.userId))
        return article.id
    }

    fun updateArticle(command: ArticleUpdateCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleCommandService.updateArticle(user.userId, command)
        if (isUpdated) {
            applicationEventPublisher.publishEvent(ArticleEvent(command.articleId, user.userId))
        }
        return isUpdated
    }

    fun deleteArticle(userId: Long, articleId: Long): Boolean {
        val user: User = getUserById(userId)

        val isDeleted: Boolean = articleCommandService.deleteArticle(user.userId, articleId)
        if (isDeleted) {
            applicationEventPublisher.publishEvent(ArticleEvent(articleId, user.userId))
        }
        return isDeleted
    }

    fun likeArticle(command: ArticleLikeCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleCommandService.likeArticle(user.userId, command.articleId)
        if (isUpdated) {
            applicationEventPublisher.publishEvent(ArticleLikeEvent(command.articleId, user.userId, true))
        }
        return isUpdated
    }

    fun undoLikeArticle(command: ArticleUndoLikeCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleCommandService.undoLikeArticle(user.userId, command.articleId)
        if (isUpdated) {
            applicationEventPublisher.publishEvent(ArticleLikeEvent(command.articleId, user.userId, false))
        }
        return isUpdated
    }

    private fun getUserById(userId: Long): User {
        return userHttpClient.findUserByUserId(userId)
            ?: throw UserNotFoundException()
    }
}
