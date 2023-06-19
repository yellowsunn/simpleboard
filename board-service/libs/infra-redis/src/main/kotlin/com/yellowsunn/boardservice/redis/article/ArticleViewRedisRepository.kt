package com.yellowsunn.boardservice.redis.article

import com.yellowsunn.boardservice.query.repository.ArticleViewCacheRepository
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component

@Component
class ArticleViewRedisRepository(
    private val stringRedisTemplate: StringRedisTemplate,
    private val viewCountPopScript: RedisScript<Long>,
) : ArticleViewCacheRepository {
    private companion object {
        private const val KEY = "article-views"
    }

    override fun increaseViewCount(articleId: Long, count: Long): Long {
        val hashOperations = stringRedisTemplate.opsForHash<String, Long>()
        return hashOperations.increment(KEY, articleId.toString(), count)
    }

    override fun findViewCounts(articleIds: List<Long>): Map<Long, Long> {
        val hashOperations = stringRedisTemplate.opsForHash<String, String>()

        val viewCounts: List<String?> = hashOperations.multiGet(KEY, articleIds.map { it.toString() })

        return articleIds.mapIndexed { index, articleId ->
            articleId to (viewCounts[index]?.toLong() ?: 0L)
        }.toMap()
    }

    override fun popViewCount(articleId: Long): Long {
        return stringRedisTemplate.execute(viewCountPopScript, listOf(KEY, articleId.toString()))
    }

    override fun findArticleIds(): List<Long> {
        val hashOperations = stringRedisTemplate.opsForHash<String, String>()

        val articleIds: MutableList<Long> = mutableListOf()
        val entry = hashOperations.scan(KEY, ScanOptions.NONE)

        entry.forEach {
            articleIds.add(it.key.toLong())
        }
        entry.close()
        return articleIds
    }
}
