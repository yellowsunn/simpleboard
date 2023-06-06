package com.yellowsunn.boardservice.persistence

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.util.Assert

open class BaseJpaRepository<T>(
    private val em: EntityManager,
) {
    @Transactional
    open fun save(entity: T): T {
        Assert.notNull(entity, "Entity must not be null")
        em.persist(entity)
        em.flush()
        return entity
    }
}
