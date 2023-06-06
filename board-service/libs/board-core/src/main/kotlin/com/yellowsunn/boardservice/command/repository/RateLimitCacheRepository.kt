package com.yellowsunn.boardservice.command.repository

import java.time.Duration

interface RateLimitCacheRepository {
    // 특정 시간동안 전달되는 요청중 최초 한번의 요청만 수락한다.
    fun acquireJustOne(key: String, duration: Duration): Boolean
}
