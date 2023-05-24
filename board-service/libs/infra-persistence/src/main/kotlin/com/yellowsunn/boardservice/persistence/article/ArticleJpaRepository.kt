package com.yellowsunn.boardservice.persistence.article

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.command.article.QArticle.article
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
    override fun findById(id: Long): Article? {
        return jpaQueryFactory
            .selectFrom(article)
            .where(article.id.eq(id))
            .fetchFirst()
    }
}
