package com.yellowsunn.boardservice.domain.article

import com.yellowsunn.boardservice.domain.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass

@Entity
@IdClass(ArticleLikeId::class)
class ArticleLike(
    @Id
    val articleId: Long,
    @Id
    val userId: Long,
) : BaseTimeEntity()
