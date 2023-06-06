package com.yellowsunn.boardservice.command.domain.article

import java.io.Serializable

data class ArticleLikeId(
    val articleId: Long = 0L,
    val userId: Long = 0L,
) : Serializable
