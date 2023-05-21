package com.yellowsunn.boardservice.facade

import com.yellowsunn.boardservice.domain.article.Article
import com.yellowsunn.boardservice.domain.user.User
import com.yellowsunn.boardservice.dto.ArticleSaveCommand
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
    fun saveArticle(command: ArticleSaveCommand): String {
        val user: User = userHttpClient.findUserByUserUUID(command.userUUID)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val article: Article = articleService.saveArticle(user.userId, command.title, command.body)

        articleEventProducer.sendEvent(articleId = article.id)
        return article.uuid
    }
}
