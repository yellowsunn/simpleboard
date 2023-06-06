package com.yellowsunn.boardservice.query.repository

interface ArticleViewCacheRepository {
    fun increaseViewCount(articleId: Long): Long

    // key: articleId, value: viewCount
    fun findViewCounts(articleIds: List<Long>): Map<Long, Long>
}
