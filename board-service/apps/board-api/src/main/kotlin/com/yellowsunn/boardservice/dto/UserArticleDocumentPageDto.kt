package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import org.springframework.data.domain.Page
import java.time.ZonedDateTime

data class UserArticleDocumentPageDto(
    val articles: List<UserArticleDocumentDto>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val numberOfElements: Int,
    val totalElements: Long,
) {
    data class UserArticleDocumentDto(
        val articleId: Long,
        val title: String,
        val savedAt: ZonedDateTime,
    )

    companion object {
        fun from(
            documentPage: Page<ArticleDocument>,
        ): UserArticleDocumentPageDto {
            val articles: List<UserArticleDocumentDto> = documentPage.content.map {
                UserArticleDocumentDto(
                    articleId = it.articleId,
                    title = it.title,
                    savedAt = it.savedAt,
                )
            }

            return UserArticleDocumentPageDto(
                articles = articles,
                page = documentPage.number + 1,
                size = documentPage.size,
                totalPages = documentPage.totalPages,
                numberOfElements = documentPage.numberOfElements,
                totalElements = documentPage.totalElements,
            )
        }
    }
}
