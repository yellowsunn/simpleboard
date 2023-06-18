package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.query.domain.article.ArticleReactionDocument

data class ArticleReactionDocumentDto(
    val articleId: Long,
    val isArticleLiked: Boolean,
    val likedCommentIds: List<Long>,
) {
    companion object {
        fun from(document: ArticleReactionDocument): ArticleReactionDocumentDto {
            return ArticleReactionDocumentDto(
                articleId = document.articleId,
                isArticleLiked = document.isArticleLiked,
                likedCommentIds = document.likedCommentIds,
            )
        }
    }
}
