package com.yellowsunn.boardservice.domain.command.article

import com.yellowsunn.boardservice.domain.command.BaseTimeEntity
import com.yellowsunn.boardservice.utils.sanitizeHtml
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.apache.commons.text.StringEscapeUtils

@Entity
class Article(
    var title: String,
    body: String,
    val userId: Long,
) : BaseTimeEntity() {
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var body: String = filterAndEscapeHtmlTag(body)

    var viewCount: Long = 0L
        private set

    // 이스케이프 문자로 저장된 데이터를, 로우한 HTML 데이터로 변환
    fun unescapedBody(): String {
        return StringEscapeUtils.unescapeHtml4(body)
    }

    fun updateTitleAndBody(title: String, body: String): Boolean {
        if (this.title == title && this.body == body) {
            return false
        }
        this.title = title
        this.body = filterAndEscapeHtmlTag(body)
        return true
    }

    // HTML 태그를 필터링하고, 이스케이프 문자로 치환한다.
    private fun filterAndEscapeHtmlTag(html: String): String {
        return StringEscapeUtils.escapeHtml4(sanitizeHtml(html))
    }
}
