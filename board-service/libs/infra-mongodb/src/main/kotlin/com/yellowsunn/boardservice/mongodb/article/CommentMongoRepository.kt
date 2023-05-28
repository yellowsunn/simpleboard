package com.yellowsunn.boardservice.mongodb.article

import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import com.yellowsunn.boardservice.query.repository.CommentDocumentRepository
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
import kotlin.math.max

@Component
class CommentMongoRepository(
    private val delegate: CommentMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : CommentDocumentRepository {
    override fun upsertByCommentId(commentId: Long, entity: CommentDocument): CommentDocument? {
        val query = Query(Criteria.where(COMMENT_ID).`is`(commentId))
        val options = FindAndReplaceOptions()
            .upsert()
            .returnNew()

        return mongoTemplate.findAndReplace(query, entity, options)
    }

    override fun findByCommentId(commentId: Long): CommentDocument? {
        return delegate.findByCommentId(commentId)
    }

    override fun findComments(articleId: Long, page: Int, size: Int): Page<CommentDocument> {
        val pageable = PageRequest.of(max(page, 0), size)
        val query = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
            .addCriteria(Criteria.where(IS_DELETED).`is`(false))
            .with(pageable)

        val comments: List<CommentDocument> = mongoTemplate.find(
            Query.of(query)
                .with(
                    Sort.by(
                        listOf(
                            Sort.Order(Sort.Direction.DESC, BASE_COMMENT_ID),
                            Sort.Order(Sort.Direction.DESC, COMMENT_ID),
                        ),
                    ),
                ),
            CommentDocument::class.java,
        )

        return PageableExecutionUtils.getPage(comments, pageable) {
            mongoTemplate.count(Query.of(query).skip(-1).limit(-1), CommentDocument::class.java)
        }
    }

    override fun findOffsetByCommentId(articleId: Long, baseCommentId: Long, commentId: Long): Long {
        // baseCommentId가 더 큰 쿼리
        val query1 = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
            .addCriteria(Criteria.where(BASE_COMMENT_ID).gt(baseCommentId))
            .addCriteria(Criteria.where(IS_DELETED).`is`(false))

        // baseCommentId가 같은 쿼리
        val query2 = Query(Criteria.where(ARTICLE_ID).`is`(articleId))
            .addCriteria(Criteria.where(BASE_COMMENT_ID).`is`(baseCommentId))
            .addCriteria(Criteria.where(COMMENT_ID).gte(commentId))
            .addCriteria(Criteria.where(IS_DELETED).`is`(false))

        val count = mongoTemplate.count(query1, CommentDocument::class.java) +
            mongoTemplate.count(query2, CommentDocument::class.java)

        return max(count - 1L, 0L)
    }

    private companion object {
        private const val COMMENT_ID = "commentId"
        private const val ARTICLE_ID = "articleId"
        private const val BASE_COMMENT_ID = "baseCommentId"
        private const val IS_DELETED = "isDeleted"
    }
}

interface CommentMongoRepositoryDelegate : MongoRepository<CommentDocument, String> {
    fun findByCommentId(commentId: Long): CommentDocument?
}
