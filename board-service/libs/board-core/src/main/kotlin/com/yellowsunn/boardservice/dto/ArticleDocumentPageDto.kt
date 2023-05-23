package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
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
        val id: String,
        val articleId: Long,
        val title: String,
        val readCount: Long,
        val likeCount: Long,
        val savedAt: ZonedDateTime,
    )

    companion object {
        fun from(documentPage: Page<ArticleDocument>, viewCounts: Map<Long, Long>): ArticleDocumentPageDto {
            val articles = documentPage.content.map {
                convertArticleDocumentDto(it, viewCounts[it.articleId] ?: 0L)
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

        private fun convertArticleDocumentDto(document: ArticleDocument, increasedViewCount: Long) = ArticleDocumentDto(
            id = document.id,
            articleId = document.articleId,
            title = document.title,
            readCount = document.readCount + increasedViewCount,
            likeCount = document.likeCount,
            savedAt = document.savedAt,
        )
    }
}
