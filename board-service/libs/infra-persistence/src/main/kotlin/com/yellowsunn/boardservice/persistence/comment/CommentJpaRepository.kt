package com.yellowsunn.boardservice.persistence.comment

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

    override fun findById(id: Long): Comment? {
        return jpaQueryFactory
            .selectFrom(comment)
            .where(comment.id.eq(id))
            .fetchOne()
    }
}
