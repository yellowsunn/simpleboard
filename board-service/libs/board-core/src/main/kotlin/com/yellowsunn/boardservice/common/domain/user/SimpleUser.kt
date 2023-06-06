package com.yellowsunn.boardservice.common.domain.user

data class SimpleUser(
    val userId: Long,
    val uuid: String,
    val nickName: String,
    val thumbnail: String?,
)
