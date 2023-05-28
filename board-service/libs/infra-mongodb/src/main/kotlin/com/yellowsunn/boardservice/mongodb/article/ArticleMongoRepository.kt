package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import com.yellowsunn.boardservice.query.repository.ArticleDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
        val query = Query(Criteria.where(IS_DELETED).`is`(false))
            .with(pageable)

        val articles: List<ArticleDocument> = mongoTemplate.find(
            Query.of(query).with(Sort.by(Sort.Direction.DESC, SAVED_AT)),
            ArticleDocument::class.java,
        )

        return PageableExecutionUtils.getPage(articles, pageable) {
            mongoTemplate.count(Query.of(query).skip(-1).limit(-1), ArticleDocument::class.java)
        }
    }

    private companion object {
        private const val OBJECT_ID_LENGTH = 24
        private const val ARTICLE_ID = "articleId"
        private const val SAVED_AT = "savedAt"
        private const val IS_DELETED = "isDeleted"
    }
}

interface ArticleMongoRepositoryDelegate : MongoRepository<ArticleDocument, String> {
    fun findByArticleId(articleId: Long): ArticleDocument?
}
