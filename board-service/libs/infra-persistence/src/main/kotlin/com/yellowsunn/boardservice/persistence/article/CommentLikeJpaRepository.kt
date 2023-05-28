package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.command.domain.comment.CommentLike
import com.yellowsunn.boardservice.command.domain.comment.QCommentLike.commentLike
import com.yellowsunn.boardservice.command.repository.CommentLikeRepository
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CommentLikeJpaRepository(
    em: EntityManager,
) : CommentLikeRepository,
    BaseJpaRepository<CommentLike>(em) {

    private val jpaQueryFactory = JPAQueryFactory(em)

    @Transactional(readOnly = true)
    override fun countByCommentId(commentId: Long): Long {
        return jpaQueryFactory
            .select(commentLike.count())
            .from(commentLike)
            .where(commentLike.commentId.eq(commentId))
            .fetchOne() ?: 0L
    }
}
