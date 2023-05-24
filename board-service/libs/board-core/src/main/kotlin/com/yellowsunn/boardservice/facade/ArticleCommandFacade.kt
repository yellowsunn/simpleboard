package com.yellowsunn.boardservice.facade

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.user.User
import com.yellowsunn.boardservice.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.dto.ArticleSaveCommand
import com.yellowsunn.boardservice.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.dto.ArticleUpdateCommand
import com.yellowsunn.boardservice.event.ArticleLikeSyncEvent
import com.yellowsunn.boardservice.event.ArticleSyncEvent
import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.service.ArticleCommandService
import com.yellowsunn.common.exception.UserNotFoundException
import org.springframework.stereotype.Component

@Component
class ArticleCommandFacade(
    private val userHttpClient: UserHttpClient,
    private val articleCommandService: ArticleCommandService,
    private val articleEventProducer: ArticleEventProducer,
) {
    fun saveArticle(command: ArticleSaveCommand): Long {
        val user: User = getUserByUUID(command.userUUID)

        val article: Article = articleCommandService.saveArticle(user.userId, command.title, command.body)

        articleEventProducer.syncArticleEvent(ArticleSyncEvent(article.id))
        return article.id
    }

    fun updateArticle(command: ArticleUpdateCommand): Boolean {
        val user: User = getUserByUUID(command.userUUID)

        val isUpdated: Boolean = articleCommandService.updateArticle(user.userId, command)
        if (isUpdated) {
            articleEventProducer.syncArticleEvent(ArticleSyncEvent(command.articleId))
        }
        return isUpdated
    }

    fun deleteArticle(userUUID: String, articleId: Long): Boolean {
        val user: User = getUserByUUID(userUUID)

        val isDeleted: Boolean = articleCommandService.deleteArticle(user.userId, articleId)
        if (isDeleted) {
            articleEventProducer.syncArticleEvent(ArticleSyncEvent(articleId))
        }
        return isDeleted
    }

    fun likeArticle(command: ArticleLikeCommand): Boolean {
        val user: User = getUserByUUID(command.userUUID)

        val isUpdated: Boolean = articleCommandService.likeArticle(user.userId, command.articleId)
        if (isUpdated) {
            articleEventProducer.syncArticleLikeEvent(ArticleLikeSyncEvent(command.articleId))
        }
        return isUpdated
    }

    fun undoLikeArticle(command: ArticleUndoLikeCommand): Boolean {
        val user: User = getUserByUUID(command.userUUID)

        val isUpdated: Boolean = articleCommandService.undoLikeArticle(user.userId, command.articleId)
        if (isUpdated) {
            articleEventProducer.syncArticleLikeEvent(ArticleLikeSyncEvent(command.articleId))
        }
        return isUpdated
    }

    private fun getUserByUUID(uuid: String): User {
        return userHttpClient.findUserByUserUUID(uuid)
            ?: throw UserNotFoundException()
    }
}
