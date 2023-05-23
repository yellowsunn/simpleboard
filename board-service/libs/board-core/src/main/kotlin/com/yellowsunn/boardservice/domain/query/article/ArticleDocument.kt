package com.yellowsunn.boardservice.domain.query.article

import com.yellowsunn.boardservice.domain.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "articles")
class ArticleDocument(
    var articleId: Long,
    var title: String,
    var body: String,
    var readCount: Long,
    var likeCount: Long,
    val userId: Long,
    val savedAt: ZonedDateTime,
) : BaseDocumentEntity()
