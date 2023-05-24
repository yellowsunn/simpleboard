package com.yellowsunn.boardservice.mongodb.article

import com.mongodb.client.result.UpdateResult
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component

@Component
class ArticleMongoRepository(
    private val delegate: ArticleMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : ArticleQueryRepository {
    override fun save(entity: ArticleDocument): ArticleDocument {
        return delegate.save(entity)
    }

    override fun findById(id: String): ArticleDocument? {
        return delegate.findByIdOrNull(id)
    }

    override fun findArticles(page: Int, size: Int): Page<ArticleDocument> {
        val pageable = PageRequest.of(page, size)
        val query = Query()
            .with(pageable)

        val articles: List<ArticleDocument> = mongoTemplate.find(
            Query.of(query).with(Sort.by(Sort.Direction.DESC, "savedAt")),
            ArticleDocument::class.java,
        )

        return PageableExecutionUtils.getPage(articles, pageable) {
            mongoTemplate.count(Query.of(query).skip(-1).limit(-1), ArticleDocument::class.java)
        }
    }

    override fun updateLikeCount(articleId: Long, likeCount: Long): Boolean {
        val query = Query(Criteria.where("articleId").`is`(articleId))
        val update = Update.update("likeCount", likeCount)

        val updateResult: UpdateResult = mongoTemplate.updateFirst(query, update, ArticleDocument::class.java)

        return updateResult.wasAcknowledged()
    }
}

interface ArticleMongoRepositoryDelegate : MongoRepository<ArticleDocument, String>
