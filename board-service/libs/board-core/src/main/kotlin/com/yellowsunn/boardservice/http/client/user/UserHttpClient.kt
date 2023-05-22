package com.yellowsunn.boardservice.http.client.user

import com.yellowsunn.boardservice.domain.user.User

interface UserHttpClient {
    fun findUserByUserUUID(userUUID: String): User?
}
