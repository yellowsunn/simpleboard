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
    val title: String,
    body: String,
    val userId: Long,
) : BaseTimeEntity() {
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    // HTML 태그를 필터링하고, 이스케이프 문자로 치환한다.
    val body: String = StringEscapeUtils.escapeHtml4(filterHtmlTag(body))

    var viewCount: Long = 0L
        private set

    // 이스케이프 문자로 저장된 데이터를, 로우한 HTML 데이터로 변환
    fun unescapedBody(): String {
        return StringEscapeUtils.unescapeHtml4(body)
    }

    // 허용하는 HTML 태그만 필터링
    private fun filterHtmlTag(html: String): String {
        return sanitizeHtml(html = html)
    }
}
