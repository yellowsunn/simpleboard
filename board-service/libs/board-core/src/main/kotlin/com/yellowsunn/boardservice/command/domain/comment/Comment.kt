package com.yellowsunn.boardservice.command.domain.comment

import com.yellowsunn.boardservice.command.domain.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Comment(
    val articleId: Long,
    val userId: Long,
    content: String?,
    imageUrl: String? = null,
    val parentCommentId: Long? = null,
    var baseCommentId: Long = -1L,
) : BaseTimeEntity() {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var content: String? = content
        private set

    var imageUrl: String? = imageUrl
        private set

    var likeCount: Long = 0L
        private set

    fun changeBaseCommentId(baseCommentId: Long) {
        this.baseCommentId = baseCommentId
    }

    fun isReply(): Boolean {
        return parentCommentId != null
    }
}
