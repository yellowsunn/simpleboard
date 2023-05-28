package com.yellowsunn.boardservice.query.repository

import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import org.springframework.data.domain.Page

interface ArticleDocumentRepository {
    fun upsertByArticleId(articleId: Long, entity: ArticleDocument): ArticleDocument?
    fun findById(id: String): ArticleDocument?
    fun findByArticleId(id: Long): ArticleDocument?
    fun findArticles(page: Int, size: Int): Page<ArticleDocument>
}
