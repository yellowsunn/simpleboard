package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.repository.article.ArticleQueryRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class ArticleMongoRepository(
    private val delegate: ArticleMongoRepositoryDelegate,
) : ArticleQueryRepository {
    override fun save(entity: ArticleDocument): ArticleDocument {
        return delegate.save(entity)
    }
}

interface ArticleMongoRepositoryDelegate : MongoRepository<ArticleDocument, String>
