package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.query.domain.article.ArticleReactionDocument
import com.yellowsunn.boardservice.query.repository.ArticleReactionDocumentRepository
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class ArticleReactionMongoRepository(
    private val delegate: ArticleReactionMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : ArticleReactionDocumentRepository {
    override fun upsertByArticleIdAndUserId(
        articleId: Long,
        userId: Long,
        entity: ArticleReactionDocument,
    ): ArticleReactionDocument? {
        val query = Query(
            Criteria().andOperator(
                Criteria.where(ARTICLE_ID).`is`(articleId),
                Criteria.where(USER_ID).`is`(userId),
            ),
        )
        val options = FindAndReplaceOptions()
            .upsert()
            .returnNew()

        return mongoTemplate.findAndReplace(query, entity, options)
    }

    override fun findByArticleIdAndUserId(articleId: Long, userId: Long): ArticleReactionDocument? {
        return delegate.findByArticleIdAndUserId(articleId, userId)
    }

    private companion object {
        private const val ARTICLE_ID = "articleId"
        private const val USER_ID = "userId"
    }
}

interface ArticleReactionMongoRepositoryDelegate : MongoRepository<ArticleReactionDocument, String> {
    fun findByArticleIdAndUserId(articleId: Long, userId: Long): ArticleReactionDocument?
}
