package com.yellowsunn.boardservice.query.domain.article

import com.yellowsunn.boardservice.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "article_reactions")
class ArticleReactionDocument(
    val articleId: Long,
    val userId: Long,
    val isArticleLiked: Boolean = false,
    val likedCommentIds: List<Long> = emptyList(),
) : BaseDocumentEntity()
