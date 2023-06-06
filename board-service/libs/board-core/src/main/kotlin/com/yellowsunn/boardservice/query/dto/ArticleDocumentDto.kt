package com.yellowsunn.boardservice.query.dto

import com.yellowsunn.boardservice.common.domain.user.SimpleUser
import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import org.apache.commons.text.StringEscapeUtils
import java.time.ZonedDateTime

data class ArticleDocumentDto(
    val id: String,
    val articleId: Long,
    val title: String,
    val body: String,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long,
    val savedAt: ZonedDateTime,
    val user: ArticleDocumentUser?,
) {
    companion object {
        fun from(articleDocument: ArticleDocument, users: Map<Long, SimpleUser>, increasedViewCount: Long) =
            ArticleDocumentDto(
                id = articleDocument.id,
                articleId = articleDocument.articleId,
                title = articleDocument.title,
                body = StringEscapeUtils.unescapeHtml4(articleDocument.body),
                viewCount = articleDocument.viewCount + increasedViewCount,
                likeCount = articleDocument.likeCount,
                commentCount = articleDocument.commentCount,
                savedAt = articleDocument.savedAt,
                user = ArticleDocumentUser.from(users[articleDocument.userId]),
            )
    }

    data class ArticleDocumentUser(
        val uuid: String,
        val thumbnail: String?,
        val nickName: String,
    ) {
        companion object {
            fun from(user: SimpleUser?): ArticleDocumentUser? {
                return user?.let {
                    ArticleDocumentUser(it.uuid, it.thumbnail, it.nickName)
                }
            }
        }
    }
}
