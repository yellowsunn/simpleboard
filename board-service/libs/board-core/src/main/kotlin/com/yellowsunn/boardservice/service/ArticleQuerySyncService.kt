package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
import com.yellowsunn.boardservice.repository.article.ArticleQueryRepository
import org.springframework.stereotype.Service

@Service
class ArticleQuerySyncService(
    private val articleLikeRepository: ArticleLikeRepository,
    private val articleQueryRepository: ArticleQueryRepository,
) {
    fun syncArticleLike(articleId: Long): Boolean {
        val likeCount: Long = articleLikeRepository.countByArticleId(articleId)

        return articleQueryRepository.updateLikeCount(articleId, likeCount)
    }
}
