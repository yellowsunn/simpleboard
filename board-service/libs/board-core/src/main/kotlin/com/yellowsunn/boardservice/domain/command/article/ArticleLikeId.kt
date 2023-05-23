package com.yellowsunn.boardservice.domain.command.article

import java.io.Serializable

data class ArticleLikeId(
    val articleId: Long,
    val userId: Long,
) : Serializable
