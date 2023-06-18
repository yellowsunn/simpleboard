package com.yellowsunn.boardservice.dto

import java.time.ZonedDateTime

data class EventSendFailureDto(
    val id: Long,
    val topic: String,
    val data: String,
    var isUsed: Boolean,
    val modifiedAt: ZonedDateTime,
)
