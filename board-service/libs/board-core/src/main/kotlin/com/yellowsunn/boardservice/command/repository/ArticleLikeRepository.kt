package com.yellowsunn.boardservice.command.repository

import com.yellowsunn.boardservice.command.domain.article.ArticleLike
import com.yellowsunn.boardservice.command.domain.article.ArticleLikeId

interface ArticleLikeRepository {
    fun save(entity: ArticleLike): ArticleLike
    fun deleteById(id: ArticleLikeId): Boolean
    fun countByArticleId(articleId: Long): Long
    fun existsByArticleIdAndUserId(articleId: Long, userId: Long): Boolean
}
