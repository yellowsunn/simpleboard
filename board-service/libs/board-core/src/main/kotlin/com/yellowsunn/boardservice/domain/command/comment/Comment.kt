package com.yellowsunn.boardservice.domain.command.comment

import com.yellowsunn.boardservice.domain.command.BaseTimeEntity
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
    parentCommentId: Long? = null,
    parentUserId: Long? = null,
    imageUrl: String? = null,
) : BaseTimeEntity() {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var content: String? = content
        private set

    var parentCommentId: Long? = parentCommentId
        private set

    var parentUserId: Long? = parentCommentId
        private set

    var imageUrl: String? = imageUrl
        private set

    var likeCount: Long = 0L
        private set

    fun isReply(): Boolean {
        return parentCommentId != null
    }
}
