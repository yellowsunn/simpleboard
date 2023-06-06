package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.query.domain.article.ArticleReactionDocument
import com.yellowsunn.boardservice.mongodb.MongoIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

class ArticleReactionMongoRepositoryIntegrationTest : MongoIntegrationTest() {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var delegate: ArticleReactionMongoRepositoryDelegate

    private lateinit var articleReactionMongoRepository: ArticleReactionMongoRepository

    @BeforeEach
    fun setUp() {
        articleReactionMongoRepository = ArticleReactionMongoRepository(delegate, mongoTemplate)
    }

    @Test
    fun upsertByArticleIdAndUserId() {
        val articleId = 1L
        val userId = 1L
        val articleReactionDocument = ArticleReactionDocument(
            articleId = articleId,
            userId = userId,
            isArticleLiked = true,
        )

        val savedDocument =
            articleReactionMongoRepository.upsertByArticleIdAndUserId(articleId, userId, articleReactionDocument)

        assertThat(savedDocument!!.id).isNotBlank
    }
}
