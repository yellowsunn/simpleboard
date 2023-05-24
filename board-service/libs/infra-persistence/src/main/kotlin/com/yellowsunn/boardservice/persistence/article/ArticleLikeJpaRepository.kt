package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.domain.command.article.ArticleLike
import com.yellowsunn.boardservice.domain.command.article.ArticleLikeId
import com.yellowsunn.boardservice.domain.command.article.QArticleLike.articleLike
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
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
        val count: Long? = jpaQueryFactory
            .select(articleLike.count())
            .from(articleLike)
            .where(articleLike.articleId.eq(articleId))
            .fetchOne()

        return count ?: 0L
    }
}
