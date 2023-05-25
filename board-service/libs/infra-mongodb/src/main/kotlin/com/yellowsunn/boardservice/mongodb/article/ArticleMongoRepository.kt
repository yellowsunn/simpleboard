package com.yellowsunn.boardservice.mongodb.article

import com.mongodb.client.result.UpdateResult
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component

@Component
class ArticleMongoRepository(
    private val delegate: ArticleMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : ArticleDocumentRepository {
    override fun save(entity: ArticleDocument): ArticleDocument {
        return delegate.save(entity)
    }

    override fun upsertByArticleId(articleId: Long, entity: ArticleDocument): ArticleDocument? {
        val query = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
        val update = Update().apply {
            set(ARTICLE_ID, entity.articleId)
            set(TITLE, entity.title)
            set(BODY, entity.body)
            set(VIEW_COUNT, entity.viewCount)
            set(LIKE_COUNT, entity.likeCount)
            set(USER_ID, entity.userId)
            set(SAVED_AT, entity.savedAt)
            set(IS_DELETED, entity.isDeleted)
            set(THUMBNAIL, entity.thumbnail)
        }
        val options = FindAndModifyOptions()
            .upsert(true)
            .returnNew(true)

        return mongoTemplate.findAndModify(query, update, options, ArticleDocument::class.java)
    }

    override fun findById(id: String): ArticleDocument? {
        if (id.length != OBJECT_ID_LENGTH) {
            return null
        }

        return delegate.findById(id)
            .filter { it.isDeleted.not() }
            .orElse(null)
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

    override fun updateLikeCount(articleId: Long, likeCount: Long): Boolean {
        val query = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
        val update = Update.update(LIKE_COUNT, likeCount)

        val updateResult: UpdateResult = mongoTemplate.updateFirst(query, update, ArticleDocument::class.java)

        return updateResult.wasAcknowledged()
    }

    private companion object {
        private const val OBJECT_ID_LENGTH = 24
        private const val ARTICLE_ID = "articleId"
        private const val TITLE = "title"
        private const val BODY = "body"
        private const val VIEW_COUNT = "viewCount"
        private const val LIKE_COUNT = "likeCount"
        private const val USER_ID = "userId"
        private const val SAVED_AT = "savedAt"
        private const val IS_DELETED = "isDeleted"
        private const val THUMBNAIL = "thumbnail"
    }
}

interface ArticleMongoRepositoryDelegate : MongoRepository<ArticleDocument, String>
