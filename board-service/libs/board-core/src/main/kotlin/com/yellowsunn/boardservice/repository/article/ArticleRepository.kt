package com.yellowsunn.boardservice.repository.article

import com.yellowsunn.boardservice.domain.command.article.Article

interface ArticleRepository {
    fun save(entity: Article): Article
    fun findById(id: Long): Article?
    fun findByUUID(uuid: String): Article?
}
