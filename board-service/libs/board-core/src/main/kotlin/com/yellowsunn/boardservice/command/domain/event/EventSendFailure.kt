package com.yellowsunn.boardservice.command.domain.event

import com.yellowsunn.boardservice.command.domain.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

// 이벤트 발행에 실패한 경우 데이터가 저장되는 테이블
@Entity
class EventSendFailure(
    val topic: String,
    val data: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var isUsed: Boolean = false
        private set

    fun use() {
        this.isUsed = true
    }
}
