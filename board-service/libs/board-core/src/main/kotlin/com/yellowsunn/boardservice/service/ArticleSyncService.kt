package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleDocumentRepository
import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleSyncService(
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val articleDocumentRepository: ArticleDocumentRepository,
) {
    fun syncArticle(articleId: Long): Boolean {
        val article: Article = articleRepository.findById(articleId) ?: return false

        val articleDocument = ArticleDocument(
            articleId = articleId,
            title = article.title,
            body = article.body,
            viewCount = article.viewCount,
            likeCount = article.viewCount,
            userId = article.userId,
            savedAt = article.createdAt,
        )
        articleDocumentRepository.save(articleDocument)
        return true
    }

    fun syncArticleLike(articleId: Long): Boolean {
        val likeCount: Long = articleLikeRepository.countByArticleId(articleId)

        return articleDocumentRepository.updateLikeCount(articleId, likeCount)
    }
}
