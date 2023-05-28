package com.yellowsunn.boardservice.command.domain.comment

import java.io.Serializable

data class CommentLikeId(
    val commentId: Long = 0L,
    val userId: Long = 0L,
) : Serializable
