package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    @Transactional
    fun saveArticle(userId: Long, title: String, body: String): Article {
        val article = Article(
            title = title,
            body = body,
            userId = userId,
        )

        return articleRepository.save(article)
    }
}
