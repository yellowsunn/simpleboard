package com.yellowsunn.boardservice.domain.command.comment

import java.io.Serializable

data class CommentLikeId(
    val commentId: Long,
    val userId: Long,
) : Serializable
