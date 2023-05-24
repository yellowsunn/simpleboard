package com.yellowsunn.boardservice.redis.article

import com.yellowsunn.boardservice.repository.article.ArticleViewCacheRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class ArticleViewRedisRepository(
    private val stringRedisTemplate: StringRedisTemplate,
) : ArticleViewCacheRepository {
    private companion object {
        private const val KEY = "article-views"
    }

    override fun increaseViewCount(articleId: Long): Long {
        val hashOperations = stringRedisTemplate.opsForHash<String, Long>()
        return hashOperations.increment(KEY, articleId.toString(), 1)
    }

    override fun findViewCounts(articleIds: List<Long>): Map<Long, Long> {
        val hashOperations = stringRedisTemplate.opsForHash<String, String>()

        val viewCounts: List<String?> = hashOperations.multiGet(KEY, articleIds.map { it.toString() })

        return articleIds.mapIndexed { index, articleId ->
            articleId to (viewCounts[index]?.toLong() ?: 0L)
        }.toMap()
    }
}
