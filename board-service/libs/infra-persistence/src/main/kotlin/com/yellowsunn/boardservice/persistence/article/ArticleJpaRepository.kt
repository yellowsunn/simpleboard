package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.command.domain.article.QArticle.article
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Repository
class ArticleJpaRepository(
    em: EntityManager,
) : ArticleRepository,
    BaseJpaRepository<Article>(em) {

    private val jpaQueryFactory = JPAQueryFactory(em)

    @Transactional(readOnly = true)
    override fun findById(id: Long): Article? {
        return jpaQueryFactory
            .selectFrom(article)
            .where(article.id.eq(id))
            .fetchFirst()
    }

    @Transactional
    override fun updateViewCount(articleId: Long, viewCount: Long): Long {
        val clause = jpaQueryFactory.update(article)
        clause[article.viewCount] = article.viewCount.add(viewCount)
        clause[article.modifiedAt] = ZonedDateTime.now()

        return clause
            .where(article.id.eq(articleId))
            .execute()
    }
}
