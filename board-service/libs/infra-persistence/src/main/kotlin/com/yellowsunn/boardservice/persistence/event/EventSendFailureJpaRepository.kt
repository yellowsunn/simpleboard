package com.yellowsunn.boardservice.persistence.event

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import com.yellowsunn.boardservice.command.repository.EventSendFailureRepository
import com.yellowsunn.boardservice.persistence.BaseJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class EventSendFailureJpaRepository(
    em: EntityManager,
) : EventSendFailureRepository,
    BaseJpaRepository<EventSendFailure>(em)
