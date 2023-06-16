package com.yellowsunn.boardservice.command.repository

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure

interface EventSendFailureRepository {
    fun save(entity: EventSendFailure): EventSendFailure
}
