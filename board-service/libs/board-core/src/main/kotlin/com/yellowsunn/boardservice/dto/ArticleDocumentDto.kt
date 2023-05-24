package com.yellowsunn.boardservice.dto

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import org.apache.commons.text.StringEscapeUtils
import java.time.ZonedDateTime

data class ArticleDocumentDto(
    val id: String,
    val articleId: Long,
    val title: String,
    val body: String,
    val viewCount: Long,
    val likeCount: Long,
    val savedAt: ZonedDateTime,
) {
    companion object {
        fun from(articleDocument: ArticleDocument, increasedViewCount: Long) = ArticleDocumentDto(
            id = articleDocument.id,
            articleId = articleDocument.articleId,
            title = articleDocument.title,
            body = StringEscapeUtils.unescapeHtml4(articleDocument.body),
            viewCount = articleDocument.viewCount + increasedViewCount,
            likeCount = articleDocument.likeCount,
            savedAt = articleDocument.savedAt,
        )
    }
}
