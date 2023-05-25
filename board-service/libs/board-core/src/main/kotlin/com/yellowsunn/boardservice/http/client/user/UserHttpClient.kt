package com.yellowsunn.boardservice.http.client.user

import com.yellowsunn.boardservice.domain.user.SimpleUser
import com.yellowsunn.boardservice.domain.user.User

interface UserHttpClient {
    fun findUserByUserUUID(userUUID: String): User?

    // key: userId, value: SimpleUser
    fun findUsersByIds(userIds: List<Long>): Map<Long, SimpleUser>
}
