package com.yellowsunn.boardservice.persistence.article

import com.yellowsunn.boardservice.command.domain.article.ArticleLike
import com.yellowsunn.boardservice.command.domain.article.ArticleLikeId
import com.yellowsunn.boardservice.persistence.PersistenceIntegrationTest
import jakarta.persistence.EntityExistsException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

class ArticleLikeJpaRepositoryTest : PersistenceIntegrationTest() {
    @Autowired
    lateinit var em: TestEntityManager

    lateinit var articleLikeJpaRepository: ArticleLikeJpaRepository

    @BeforeEach
    fun setUp() {
        articleLikeJpaRepository = ArticleLikeJpaRepository(em.entityManager)
    }

    @Test
    fun save() {
        val articleLike = getTestArticleLike(1L)

        val savedArticleLike = articleLikeJpaRepository.save(articleLike)

        val articleLike1 = ArticleLike(
            articleId = 1L,
            userId = 1L,
        )

        try {
            articleLikeJpaRepository.save(articleLike1)
        } catch (e: EntityExistsException) {
            println()
        }

        assertThat(savedArticleLike.articleId).isEqualTo(1L)
        assertThat(savedArticleLike.userId).isEqualTo(1L)
    }

    @Test
    fun save_throws_exception_when_duplicated_like() {
        // given
        articleLikeJpaRepository.save(getTestArticleLike(1L))

        // when
        val throwable = catchThrowable { articleLikeJpaRepository.save(getTestArticleLike(1L)) }

        // then
        assertThat(throwable).isInstanceOf(EntityExistsException::class.java)
    }

    @Test
    fun deleteById() {
        // given
        articleLikeJpaRepository.save(getTestArticleLike(1L))
        val id = ArticleLikeId(
            articleId = 1L,
            userId = 1L,
        )

        val isDeleted = articleLikeJpaRepository.deleteById(id)

        assertThat(isDeleted).isTrue
    }

    @Test
    fun countByArticleId() {
        // given
        (1L..10L).forEach {
            articleLikeJpaRepository.save(getTestArticleLike(it))
        }

        // when
        val count = articleLikeJpaRepository.countByArticleId(1L)

        // then
        assertThat(count).isEqualTo(10L)
    }

    private fun getTestArticleLike(userId: Long) = ArticleLike(
        articleId = 1L,
        userId = userId,
    )
}
