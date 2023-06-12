package com.yellowsunn.boardservice.query.dto

import com.yellowsunn.boardservice.common.domain.user.SimpleUser
import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import org.springframework.data.domain.Page
import java.time.ZonedDateTime

data class ArticleDocumentPageDto(
    val articles: List<ArticleDocumentDto>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val numberOfElements: Int,
    val totalElements: Long,
) {
    data class ArticleDocumentDto(
        val articleId: Long,
        val thumbnail: String,
        val title: String,
        val viewCount: Long,
        val likeCount: Long,
        val commentCount: Long,
        val savedAt: ZonedDateTime,
        val nickName: String,
    )

    companion object {
        fun from(
            documentPage: Page<ArticleDocument>,
            viewCounts: Map<Long, Long>,
            users: Map<Long, SimpleUser>,
        ): ArticleDocumentPageDto {
            val articles = documentPage.content.map {
                convertArticleDocumentDto(
                    it,
                    users[it.userId]?.nickName ?: "",
                    viewCounts[it.articleId] ?: 0L,
                )
            }

            return ArticleDocumentPageDto(
                articles = articles,
                page = documentPage.number + 1,
                size = documentPage.size,
                totalPages = documentPage.totalPages,
                numberOfElements = documentPage.numberOfElements,
                totalElements = documentPage.totalElements,
            )
        }

        private fun convertArticleDocumentDto(
            document: ArticleDocument,
            nickName: String,
            increasedViewCount: Long,
        ): ArticleDocumentDto = ArticleDocumentDto(
            articleId = document.articleId,
            thumbnail = document.thumbnail ?: "",
            title = document.title,
            viewCount = document.viewCount + increasedViewCount,
            likeCount = document.likeCount,
            commentCount = document.commentCount,
            savedAt = document.savedAt,
            nickName = nickName,
        )
    }
}
