package com.yellowsunn.boardservice.query.repository

interface ArticleViewCacheRepository {
    fun increaseViewCount(articleId: Long, count: Long): Long

    // key: articleId, value: viewCount
    fun findViewCounts(articleIds: List<Long>): Map<Long, Long>

    fun popViewCount(articleId: Long): Long

    fun findArticleIds(): List<Long>
}
