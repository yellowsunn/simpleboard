package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleDocumentRepository
import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import com.yellowsunn.boardservice.utils.getFirstImageSrc
import org.springframework.stereotype.Service

@Service
class ArticleSyncService(
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val articleDocumentRepository: ArticleDocumentRepository,
) {
    fun syncArticle(articleId: Long) {
        val article: Article = articleRepository.findById(articleId) ?: return

        val articleDocument = convertToArticleDocument(article)
        articleDocumentRepository.upsertByArticleId(articleId, articleDocument)
    }

    fun syncArticleLike(articleId: Long): Boolean {
        val likeCount: Long = articleLikeRepository.countByArticleId(articleId)

        return articleDocumentRepository.updateLikeCount(articleId, likeCount)
    }

    fun convertToArticleDocument(article: Article) = ArticleDocument(
        articleId = article.id,
        thumbnail = getFirstImageSrc(article.unescapedBody()),
        title = article.title,
        body = article.body,
        viewCount = article.viewCount,
        likeCount = articleLikeRepository.countByArticleId(article.id),
        userId = article.userId,
        savedAt = article.createdAt,
        isDeleted = article.isDeleted,
    )
}
