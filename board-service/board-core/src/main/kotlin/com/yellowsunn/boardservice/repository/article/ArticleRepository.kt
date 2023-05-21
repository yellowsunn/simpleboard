package com.yellowsunn.boardservice.repository.article

import com.yellowsunn.boardservice.domain.article.Article

interface ArticleRepository {
    fun save(entity: Article): Article
    fun findByUUID(uuid: String): Article?
}
