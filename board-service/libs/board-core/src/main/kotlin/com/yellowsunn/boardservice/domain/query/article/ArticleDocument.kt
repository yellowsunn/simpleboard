package com.yellowsunn.boardservice.domain.query.article

import com.yellowsunn.boardservice.domain.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "articles")
class ArticleDocument(
    var articleId: Long,
    var uuid: String,
    var title: String,
    var body: String,
    var readCount: Long,
    var likeCount: Long,
    val userId: Long,
) : BaseDocumentEntity()
