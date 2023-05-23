package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleQueryRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleQueryService(
    private val articleRepository: ArticleRepository,
    private val articleQueryRepository: ArticleQueryRepository,
) {
    fun sync(articleId: Long): Boolean {
        val article: Article = articleRepository.findById(articleId) ?: return false

        val articleDocument = ArticleDocument(
            articleId = articleId,
            uuid = article.uuid,
            title = article.title,
            body = article.body,
            readCount = article.readCount,
            likeCount = article.readCount,
            userId = article.userId,
        )
        articleQueryRepository.save(articleDocument)
        return true
    }
}
