package com.yellowsunn.boardservice.domain.board

import com.yellowsunn.boardservice.domain.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Board(
    val name: String,
    val description: String,
    val displayOrder: Long,
) : BaseTimeEntity() {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val uuid = UUID.randomUUID().toString()
}
