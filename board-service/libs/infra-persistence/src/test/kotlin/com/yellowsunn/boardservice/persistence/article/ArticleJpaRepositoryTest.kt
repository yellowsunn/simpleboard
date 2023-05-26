package com.yellowsunn.boardservice.persistence.article

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.persistence.PersistenceIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

class ArticleJpaRepositoryTest : PersistenceIntegrationTest() {
    @Autowired
    lateinit var em: TestEntityManager

    lateinit var articleJpaRepository: ArticleJpaRepository

    @BeforeEach
    fun setUp() {
        articleJpaRepository = ArticleJpaRepository(em.entityManager)
    }

    @Test
    fun save() {
        val article = getTestArticle()

        val savedArticle = articleJpaRepository.save(article)

        assertThat(savedArticle.id).isNotNull
    }

    @Test
    fun findById() {
        val article = getTestArticle()
        articleJpaRepository.save(article)

        val foundArticle = articleJpaRepository.findById(article.id)

        assertThat(foundArticle!!.id).isNotNull
    }

    private fun getTestArticle(): Article {
        return Article(
            title = "게시글 제목",
            body = "<div><script>alert('hi')</script></div>",
            userId = 1L,
        )
    }
}
