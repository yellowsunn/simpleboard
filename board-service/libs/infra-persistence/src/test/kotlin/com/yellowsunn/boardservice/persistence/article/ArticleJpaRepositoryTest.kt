package com.yellowsunn.boardservice.persistence.article

import com.yellowsunn.boardservice.domain.article.Article
import com.yellowsunn.boardservice.persistence.PersistenceIntegrationTest
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
        val article = Article(
            title = "게시글 제목",
            body = "<div><script>alert('hi')</script></div>",
            userId = 1L,
        )

        val savedArticle = articleJpaRepository.save(article)

        println(savedArticle)
    }
}
