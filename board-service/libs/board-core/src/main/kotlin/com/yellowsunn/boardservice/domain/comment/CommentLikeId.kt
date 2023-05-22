package com.yellowsunn.boardservice.domain.comment

import java.io.Serializable

data class CommentLikeId(
    val commentId: Long,
    val userId: Long,
) : Serializable
