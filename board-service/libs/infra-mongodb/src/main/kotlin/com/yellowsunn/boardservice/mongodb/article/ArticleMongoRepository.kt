package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import com.yellowsunn.boardservice.query.repository.ArticleDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component

@Component
class ArticleMongoRepository(
    private val delegate: ArticleMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : ArticleDocumentRepository {
    override fun upsertByArticleId(articleId: Long, entity: ArticleDocument): ArticleDocument? {
        val query = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
        val options = FindAndReplaceOptions()
            .upsert()
            .returnNew()

        return mongoTemplate.findAndReplace(query, entity, options)
    }

    override fun findById(id: String): ArticleDocument? {
        if (id.length != OBJECT_ID_LENGTH) {
            return null
        }

        return delegate.findById(id)
            .filter { it.isDeleted.not() }
            .orElse(null)
    }

    override fun findByArticleId(id: Long): ArticleDocument? {
        return delegate.findByArticleId(id)
    }

    override fun findArticles(page: Int, size: Int): Page<ArticleDocument> {
        val pageable = PageRequest.of(page, size)
        return delegate.findByIsDeletedIsFalseOrderBySavedAtDesc(pageable)
    }

    override fun findUserArticles(userId: Long, page: Int, size: Int): Page<ArticleDocument> {
        val pageable = PageRequest.of(page, size)
        return delegate.findByUserIdAndIsDeletedIsFalseOrderBySavedAtDesc(userId, pageable)
    }

    private companion object {
        private const val OBJECT_ID_LENGTH = 24
        private const val ARTICLE_ID = "articleId"
        private const val SAVED_AT = "savedAt"
        private const val IS_DELETED = "isDeleted"
        private const val USER_ID = "userId"
    }
}

interface ArticleMongoRepositoryDelegate : MongoRepository<ArticleDocument, String> {
    fun findByArticleId(articleId: Long): ArticleDocument?
    fun findByIsDeletedIsFalseOrderBySavedAtDesc(pageable: Pageable): Page<ArticleDocument>
    fun findByUserIdAndIsDeletedIsFalseOrderBySavedAtDesc(userId: Long, pageable: Pageable): Page<ArticleDocument>
}
