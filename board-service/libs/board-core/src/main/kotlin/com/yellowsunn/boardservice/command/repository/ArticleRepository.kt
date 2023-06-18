package com.yellowsunn.boardservice.command.repository

import com.yellowsunn.boardservice.command.domain.article.Article

interface ArticleRepository {
    fun save(entity: Article): Article
    fun findById(id: Long): Article?
    fun updateViewCount(articleId: Long, viewCount: Long): Long
}
