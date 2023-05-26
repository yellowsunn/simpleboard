package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.command.repository.ArticleLikeRepository
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.common.utils.getFirstImageSrc
import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import com.yellowsunn.boardservice.query.domain.article.ArticleReactionDocument
import com.yellowsunn.boardservice.query.repository.ArticleDocumentRepository
import com.yellowsunn.boardservice.query.repository.ArticleReactionDocumentRepository
import org.springframework.stereotype.Service

/**
 * Command - Query 데이터 베이스 동기화하는 서비스
 */
@Service
class ArticleSyncService(
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val articleDocumentRepository: ArticleDocumentRepository,
    private val articleReactionDocumentRepository: ArticleReactionDocumentRepository,
) {
    fun syncArticleDocument(articleId: Long) {
        val article: Article = articleRepository.findById(articleId) ?: return
        val likeCount: Long = articleLikeRepository.countByArticleId(articleId)

        val articleDocument = convertToArticleDocument(article, likeCount)
        articleDocumentRepository.upsertByArticleId(articleId, articleDocument)
    }

    fun syncArticleReactionDocument(articleId: Long, userId: Long) {
        val isLiked = articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)

        val articleReactionDocument = ArticleReactionDocument(
            articleId = articleId,
            userId = userId,
            isArticleLiked = isLiked,
        )

        articleReactionDocumentRepository.upsertByArticleIdAndUserId(
            articleId = articleId,
            userId = userId,
            entity = articleReactionDocument,
        )
    }

    private fun convertToArticleDocument(article: Article, likeCount: Long) = ArticleDocument(
        articleId = article.id,
        thumbnail = getFirstImageSrc(article.unescapedBody()),
        title = article.title,
        body = article.body,
        viewCount = article.viewCount,
        likeCount = likeCount,
        userId = article.userId,
        savedAt = article.createdAt,
        isDeleted = article.isDeleted,
    )
}
