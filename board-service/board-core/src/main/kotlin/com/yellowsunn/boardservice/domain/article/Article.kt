package com.yellowsunn.boardservice.domain.article

import com.yellowsunn.boardservice.domain.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Article(
    val title: String,
    val body: String,
    val boardId: Long,
    val userId: Long,
    hasImage: Boolean,
) : BaseTimeEntity() {
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    val uuid: String = UUID.randomUUID().toString()

    var hasImage: Boolean = hasImage
        private set

    var readCount: Long = 0L
        private set

    var likeCount: Long = 0L
        private set
}
