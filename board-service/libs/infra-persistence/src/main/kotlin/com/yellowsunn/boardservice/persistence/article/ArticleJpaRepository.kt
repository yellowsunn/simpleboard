package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.domain.article.Article
import com.yellowsunn.boardservice.domain.article.QArticle.article
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleJpaRepository(
    em: EntityManager,
) : ArticleRepository,
    BaseJpaRepository<Article>(em) {

    private val jpaQueryFactory = JPAQueryFactory(em)

    @Transactional(readOnly = true)
    override fun findByUUID(uuid: String): Article? {
        return jpaQueryFactory
            .selectFrom(article)
            .where(article.uuid.eq(uuid))
            .fetchFirst()
    }
}
