package com.yellowsunn.boardservice.repository.article

import com.yellowsunn.boardservice.domain.command.article.ArticleLike
import com.yellowsunn.boardservice.domain.command.article.ArticleLikeId

interface ArticleLikeRepository {
    fun save(entity: ArticleLike): ArticleLike
    fun deleteById(id: ArticleLikeId): Boolean
    fun countByArticleId(articleId: Long): Long
}
