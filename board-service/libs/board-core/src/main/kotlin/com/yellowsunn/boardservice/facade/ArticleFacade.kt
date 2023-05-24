package com.yellowsunn.boardservice.facade

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.user.User
import com.yellowsunn.boardservice.dto.ArticleLikeCommand
import com.yellowsunn.boardservice.dto.ArticleSaveCommand
import com.yellowsunn.boardservice.dto.ArticleUndoLikeCommand
import com.yellowsunn.boardservice.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.service.ArticleService
import org.springframework.stereotype.Component

@Component
class ArticleFacade(
    private val userHttpClient: UserHttpClient,
    private val articleService: ArticleService,
    private val articleEventProducer: ArticleEventProducer,
) {
    fun saveArticle(command: ArticleSaveCommand): Long {
        val user: User = userHttpClient.findUserByUserUUID(command.userUUID)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val article: Article = articleService.saveArticle(user.userId, command.title, command.body)

        articleEventProducer.saveArticleEvent(article.id)
        return article.id
    }

    fun likeArticle(command: ArticleLikeCommand): Boolean {
        val user: User = userHttpClient.findUserByUserUUID(command.userUUID)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val isUpdated: Boolean = articleService.likeArticle(user.userId, command.articleId)
        if (isUpdated) {
            articleEventProducer.updateArticleLikeEvent(command.articleId)
        }
        return isUpdated
    }

    fun undoLikeArticle(command: ArticleUndoLikeCommand): Boolean {
        val user: User = userHttpClient.findUserByUserUUID(command.userUUID)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val isUpdated: Boolean = articleService.undoLikeArticle(user.userId, command.articleId)
        if (isUpdated) {
            articleEventProducer.updateArticleUndoLikeEvent(command.articleId)
        }
        return isUpdated
    }
}
