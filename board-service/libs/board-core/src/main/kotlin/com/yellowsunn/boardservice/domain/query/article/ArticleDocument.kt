package com.yellowsunn.boardservice.domain.query.article

import com.yellowsunn.boardservice.domain.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.index.IndexDirection.DESCENDING
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "articles")
class ArticleDocument(
    @Indexed
    var articleId: Long,
    var title: String,
    var body: String,
    var viewCount: Long,
    var likeCount: Long,
    val userId: Long,
    @Indexed(direction = DESCENDING)
    val savedAt: ZonedDateTime,
) : BaseDocumentEntity()
