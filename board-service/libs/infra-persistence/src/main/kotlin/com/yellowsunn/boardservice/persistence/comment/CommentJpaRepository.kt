package com.yellowsunn.boardservice.persistence.comment

import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.command.domain.comment.Comment
import com.yellowsunn.boardservice.command.domain.comment.QComment.comment
import com.yellowsunn.boardservice.command.repository.CommentRepository
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class CommentJpaRepository(
    em: EntityManager,
) : CommentRepository, BaseJpaRepository<Comment>(em) {

    private val jpaQueryFactory = JPAQueryFactory(em)

    override fun findById(id: Long, includeDeleted: Boolean): Comment? {
        val foundComment: Comment? = jpaQueryFactory
            .selectFrom(comment)
            .where(comment.id.eq(id))
            .fetchOne()
        if (includeDeleted) {
            return foundComment
        }

        return if (foundComment?.isDeleted == true) {
            null
        } else {
            foundComment
        }
    }

    override fun countByArticleId(articleId: Long): Long {
        return jpaQueryFactory
            .select(comment.id.count())
            .from(comment)
            .validWhere(comment.articleId.eq(articleId))
            .fetchOne() ?: 0L
    }

    private fun <T> JPAQuery<T>.validWhere(vararg o: Predicate): JPAQuery<T> {
        return this.where(*o, comment.isDeleted.isFalse)
    }
}
