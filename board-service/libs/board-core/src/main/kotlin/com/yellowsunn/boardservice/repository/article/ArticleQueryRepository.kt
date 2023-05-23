package com.yellowsunn.boardservice.repository.article

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument

interface ArticleQueryRepository {
    fun save(entity: ArticleDocument): ArticleDocument
}
