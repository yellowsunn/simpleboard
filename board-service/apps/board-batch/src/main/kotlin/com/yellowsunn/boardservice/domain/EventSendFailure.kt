package com.yellowsunn.boardservice.domain

import java.time.ZonedDateTime

data class EventSendFailure(
    val id: Long,
    val topic: String,
    val data: String,
    var isUsed: Boolean,
    val modifiedAt: ZonedDateTime,
)
