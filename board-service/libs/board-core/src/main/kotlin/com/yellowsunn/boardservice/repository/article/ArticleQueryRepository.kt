package com.yellowsunn.boardservice.repository.article

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import org.springframework.data.domain.Page

interface ArticleQueryRepository {
    fun save(entity: ArticleDocument): ArticleDocument
    fun findArticles(page: Int, size: Int): Page<ArticleDocument>
}
