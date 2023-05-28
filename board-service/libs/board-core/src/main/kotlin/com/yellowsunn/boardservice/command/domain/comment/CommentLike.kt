package com.yellowsunn.boardservice.command.domain.comment

import com.yellowsunn.boardservice.command.domain.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass

@Entity
@IdClass(CommentLikeId::class)
class CommentLike(
    @Id val commentId: Long,
    @Id val userId: Long,
    val articleId: Long,
) : BaseTimeEntity()
