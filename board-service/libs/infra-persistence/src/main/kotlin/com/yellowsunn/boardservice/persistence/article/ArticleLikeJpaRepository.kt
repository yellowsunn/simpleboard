package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.command.domain.article.ArticleLike
import com.yellowsunn.boardservice.command.domain.article.ArticleLikeId
import com.yellowsunn.boardservice.command.domain.article.QArticleLike.articleLike
import com.yellowsunn.boardservice.command.repository.ArticleLikeRepository
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleLikeJpaRepository(
    em: EntityManager,
) : ArticleLikeRepository,
    BaseJpaRepository<ArticleLike>(em) {

    private val jpaQueryFactory = JPAQueryFactory(em)

    @Transactional
    override fun deleteById(id: ArticleLikeId): Boolean {
        val count: Long = jpaQueryFactory
            .delete(articleLike)
            .where(
                articleLike.articleId.eq(id.articleId),
                articleLike.userId.eq(id.userId),
            ).execute()

        return count > 0
    }

    @Transactional(readOnly = true)
    override fun countByArticleId(articleId: Long): Long {
        return jpaQueryFactory
            .select(articleLike.count())
            .from(articleLike)
            .where(articleLike.articleId.eq(articleId))
            .fetchOne() ?: 0L
    }

    @Transactional(readOnly = true)
    override fun existsByArticleIdAndUserId(articleId: Long, userId: Long): Boolean {
        val count: Long = jpaQueryFactory
            .select(articleLike.count())
            .from(articleLike)
            .where(articleLike.articleId.eq(articleId), articleLike.userId.eq(userId))
            .fetchOne() ?: 0L

        return count > 0L
    }
}
