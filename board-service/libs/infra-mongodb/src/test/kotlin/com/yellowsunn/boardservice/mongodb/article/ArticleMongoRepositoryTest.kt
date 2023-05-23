package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.mongodb.MongoIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.ZonedDateTime

class ArticleMongoRepositoryTest : MongoIntegrationTest() {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var delegate: ArticleMongoRepositoryDelegate

    private lateinit var articleMongoRepository: ArticleMongoRepository

    @BeforeEach
    fun setUp() {
        articleMongoRepository = ArticleMongoRepository(delegate, mongoTemplate)
    }

    @Test
    fun save() {
        val articleDocument = getTestArticle(1L)

        val savedArticleDocument = articleMongoRepository.save(articleDocument)

        assertThat(savedArticleDocument.id).isNotNull
    }

    @Test
    fun findArticles() {
        (1L..10L).forEach {
            articleMongoRepository.save(getTestArticle(it))
        }

        val articleDocumentPage: Page<ArticleDocument> = articleMongoRepository.findArticles(1, 4)

        assertThat(articleDocumentPage.totalPages).isEqualTo(3)
        assertThat(articleDocumentPage.number).isEqualTo(1)
        assertThat(articleDocumentPage.content)
            .isSortedAccordingTo(Comparator.comparing<ArticleDocument, ZonedDateTime> { it.createdAt }.reversed())
    }

    private fun getTestArticle(articleId: Long): ArticleDocument = ArticleDocument(
        articleId = articleId,
        userId = 1L,
        uuid = "uuid",
        title = "title",
        body = "body",
        readCount = 0L,
        likeCount = 0L,
    )
}
