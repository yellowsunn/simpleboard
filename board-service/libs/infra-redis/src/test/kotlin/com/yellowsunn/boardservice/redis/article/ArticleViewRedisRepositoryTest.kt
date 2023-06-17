package com.yellowsunn.boardservice.redis.article

import com.yellowsunn.boardservice.redis.RedisIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript

class ArticleViewRedisRepositoryTest : RedisIntegrationTest() {
    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    lateinit var articleViewRedisRepository: ArticleViewRedisRepository

    @BeforeEach
    fun setUp() {
        val classPathResource = ClassPathResource("ViewCountPop.lua")

        articleViewRedisRepository = ArticleViewRedisRepository(
            redisTemplate,
            RedisScript.of(classPathResource, Long::class.java),
        )
    }

    @Test
    fun increaseViewCount() {
        // when
        articleViewRedisRepository.increaseViewCount(100L)
        val viewCount = articleViewRedisRepository.increaseViewCount(100L)

        // then
        assertThat(viewCount).isEqualTo(2L)
    }

    @Test
    fun getViewCounts() {
        // given
        val articleIds = (1L..5L).toList()
        listOf(2L, 4L).forEach {
            articleViewRedisRepository.increaseViewCount(it)
        }

        // when
        val viewCountMap: Map<Long, Long> = articleViewRedisRepository.findViewCounts(articleIds)

        // then
        assertThat(viewCountMap[1L]).isEqualTo(0L)
        assertThat(viewCountMap[2L]).isEqualTo(1L)
        assertThat(viewCountMap[3L]).isEqualTo(0L)
        assertThat(viewCountMap[4L]).isEqualTo(1L)
        assertThat(viewCountMap[5L]).isEqualTo(0L)
        assertThat(viewCountMap[6L]).isNull()
    }

    @Test
    fun popViewCount() {
        val articleId = 1000L
        repeat(5) {
            articleViewRedisRepository.increaseViewCount(articleId)
        }

        val viewCount = articleViewRedisRepository.popViewCount(articleId)
        assertThat(viewCount).isEqualTo(5L)
        assertThat(articleViewRedisRepository.increaseViewCount(articleId)).isEqualTo(1L)
    }

    @Test
    fun findArticleIds() {
        listOf(2000L, 3000L, 5000L).forEach {
            articleViewRedisRepository.increaseViewCount(it)
        }

        val articleIds: List<Long> = articleViewRedisRepository.findArticleIds()

        assertThat(articleIds).contains(2000L, 3000L, 5000L)
    }
}
