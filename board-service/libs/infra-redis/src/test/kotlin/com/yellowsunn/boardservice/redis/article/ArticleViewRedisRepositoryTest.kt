package com.yellowsunn.boardservice.redis.article

import com.yellowsunn.boardservice.redis.RedisIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate

class ArticleViewRedisRepositoryTest : RedisIntegrationTest() {
    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    lateinit var articleViewRedisRepository: ArticleViewRedisRepository

    @BeforeEach
    fun setUp() {
        articleViewRedisRepository = ArticleViewRedisRepository(redisTemplate)
    }

    @Test
    fun increaseViewCount() {
        // when
        articleViewRedisRepository.increaseViewCount(1L)
        val viewCount = articleViewRedisRepository.increaseViewCount(1L)

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
}
