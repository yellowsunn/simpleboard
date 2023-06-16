package com.yellowsunn.boardservice.facade

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.dto.ArticleSaveCommand
import com.yellowsunn.boardservice.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.dto.ArticleUpdateCommand
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.event.data.article.ArticleDeleteEvent
import com.yellowsunn.boardservice.event.data.article.ArticleLikeEvent
import com.yellowsunn.boardservice.event.data.article.ArticleSaveEvent
import com.yellowsunn.boardservice.event.data.article.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.event.data.article.ArticleUpdateEvent
import com.yellowsunn.boardservice.service.ArticleService
import com.yellowsunn.common.exception.LoginUserNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ArticleFacade(
    private val userHttpClient: UserHttpClient,
    private val articleService: ArticleService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun saveArticle(command: ArticleSaveCommand): Long {
        val user: User = getUserById(command.userId)

        val article: Article = articleService.saveArticle(user.userId, command.title, command.body)

        applicationEventPublisher.publishEvent(ArticleSaveEvent(article.id))
        return article.id
    }

    fun updateArticle(command: ArticleUpdateCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleService.updateArticle(user.userId, command)

        applicationEventPublisher.publishEvent(ArticleUpdateEvent(command.articleId))
        return isUpdated
    }

    fun deleteArticle(userId: Long, articleId: Long): Boolean {
        val user: User = getUserById(userId)

        val isDeleted: Boolean = articleService.deleteArticle(user.userId, articleId)

        applicationEventPublisher.publishEvent(ArticleDeleteEvent(articleId))
        return isDeleted
    }

    fun likeArticle(command: ArticleLikeCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleService.likeArticle(user.userId, command.articleId)

        applicationEventPublisher.publishEvent(ArticleLikeEvent(command.articleId, user.userId))
        return isUpdated
    }

    fun undoLikeArticle(command: ArticleUndoLikeCommand): Boolean {
        val user: User = getUserById(command.userId)

        val isUpdated: Boolean = articleService.undoLikeArticle(user.userId, command.articleId)

        applicationEventPublisher.publishEvent(ArticleUndoLikeEvent(command.articleId, user.userId))
        return isUpdated
    }

    private fun getUserById(userId: Long): User {
        return userHttpClient.findUserByUserId(userId)
            ?: throw LoginUserNotFoundException()
    }
}
