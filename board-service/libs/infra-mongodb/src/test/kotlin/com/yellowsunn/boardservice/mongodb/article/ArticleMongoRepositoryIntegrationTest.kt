package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.mongodb.MongoIntegrationTest
import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.ZonedDateTime

class ArticleMongoRepositoryIntegrationTest : MongoIntegrationTest() {
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
    fun upsertByArticleId_insert_article_when_not_found() {
        val articleDocument = getTestArticle(1L)

        val savedArticleDocument: ArticleDocument? = articleMongoRepository.upsertByArticleId(1L, articleDocument)

        assertThat(savedArticleDocument?.id).isNotNull
    }

    @Test
    fun upsertByArticleId_update_article_when_found() {
        val articleDocument = getTestArticle(1L)
        val savedArticleDocument: ArticleDocument? = articleMongoRepository.upsertByArticleId(1L, articleDocument)
        articleDocument.title = "updated_title"
        articleDocument.body = "updated_body"

        val updatedArticleDocument: ArticleDocument? = articleMongoRepository.upsertByArticleId(1L, articleDocument)

        assertThat(updatedArticleDocument?.id).isNotNull
        assertThat(savedArticleDocument!!.id).isEqualTo(updatedArticleDocument!!.id)
        assertThat(articleDocument.title).isEqualTo("updated_title")
        assertThat(articleDocument.body).isEqualTo("updated_body")
    }

    @Test
    fun findById() {
        val articleDocument = getTestArticle(1L)
        val upsertedArticleDocument = articleMongoRepository.upsertByArticleId(1L, articleDocument)

        val foundDocument: ArticleDocument? = articleMongoRepository.findById(upsertedArticleDocument!!.id)

        assertThat(foundDocument).isNotNull
        assertThat(foundDocument!!.id).isEqualTo(upsertedArticleDocument.id)
    }

    @Test
    fun findArticles() {
        (1L..10L).forEach {
            articleMongoRepository.upsertByArticleId(it, getTestArticle(it))
        }

        val articleDocumentPage: Page<ArticleDocument> = articleMongoRepository.findArticles(1, 4)

        assertThat(articleDocumentPage.totalPages).isEqualTo(3)
        assertThat(articleDocumentPage.number).isEqualTo(1)
        assertThat(articleDocumentPage.content)
            .isSortedAccordingTo(Comparator.comparing<ArticleDocument, ZonedDateTime> { it.savedAt }.reversed())
    }

    private fun getTestArticle(articleId: Long): ArticleDocument = ArticleDocument(
        articleId = articleId,
        userId = 1L,
        title = "title",
        body = "body",
        viewCount = 0L,
        likeCount = 0L,
        savedAt = ZonedDateTime.now(),
    )
}
