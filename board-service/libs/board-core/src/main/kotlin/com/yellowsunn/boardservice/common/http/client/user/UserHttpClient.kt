package com.yellowsunn.boardservice.common.http.client.user

import com.yellowsunn.boardservice.common.domain.user.SimpleUser
import com.yellowsunn.boardservice.common.domain.user.User

interface UserHttpClient {
    fun findUserByUserUUID(userUUID: String): User?

    // key: userId, value: SimpleUser
    fun findUsersByIds(userIds: List<Long>): Map<Long, SimpleUser>
}
