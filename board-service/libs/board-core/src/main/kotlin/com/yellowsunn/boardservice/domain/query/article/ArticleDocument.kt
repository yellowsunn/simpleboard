package com.yellowsunn.boardservice.domain.query.article

import com.yellowsunn.boardservice.domain.query.BaseDocumentEntity
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "articles")
@CompoundIndexes(
    CompoundIndex(def = "{'isDeleted': 1, 'savedAt': -1}"),
)
class ArticleDocument(
    @Indexed
    var articleId: Long,
    var title: String,
    var body: String,
    var viewCount: Long = 0L,
    var likeCount: Long = 0L,
    val userId: Long,
    val savedAt: ZonedDateTime,
    var isDeleted: Boolean = false,
) : BaseDocumentEntity()
