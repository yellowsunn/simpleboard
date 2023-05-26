package com.yellowsunn.boardservice.query.repository

import com.yellowsunn.boardservice.query.domain.article.ArticleReactionDocument

interface ArticleReactionDocumentRepository {
    fun upsertByArticleIdAndUserId(
        articleId: Long,
        userId: Long,
        entity: ArticleReactionDocument,
    ): ArticleReactionDocument?

    fun findByArticleIdAndUserId(articleId: Long, userId: Long): ArticleReactionDocument?
}
