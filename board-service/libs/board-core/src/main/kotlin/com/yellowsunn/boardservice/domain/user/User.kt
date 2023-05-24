package com.yellowsunn.boardservice.domain.user

data class User(
    val userId: Long,
    val uuid: String,
    val email: String,
    val nickName: String,
    val thumbnail: String,
    val providers: List<String>,
)