package com.yellowsunn.boardservice.command.domain.comment

import java.io.Serializable

data class CommentLikeId(
    val commentId: Long,
    val userId: Long,
) : Serializable
