package com.yellowsunn.boardservice.common.utils

import org.owasp.html.HtmlPolicyBuilder
import org.owasp.html.PolicyFactory

// 허용하는 html 태그만 필터링
fun sanitizeHtml(html: String): String {
    val policyFactory: PolicyFactory = HtmlPolicyBuilder()
        .allowElements(
            "a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4",
            "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead",
            "tr", "u", "ul", "link", "figure", "oembed",
        )
        .allowAttributes("class").onElements("figure")
        .allowAttributes("url").onElements("oembed")
        .allowAttributes("alt", "align", "title", "img", "src").onElements("img")
        .allowAttributes("href", "target", "class").onElements("a")
        .allowAttributes("class", "id", "style").onElements("div")
        .allowAttributes("id").onElements("ul")
        .allowAttributes("class").onElements("li")
        .allowAttributes("rel", "href", "media").onElements("link")
        .allowStandardUrlProtocols()
        .toFactory()

    return policyFactory.sanitize(html)
}

fun getFirstImageSrc(html: String): String {
    val policyFactory: PolicyFactory = HtmlPolicyBuilder()
        .allowElements("img")
        .allowAttributes("alt", "align", "title", "img", "src").onElements("img")
        .allowStandardUrlProtocols()
        .toFactory()

    val imageTags = policyFactory.sanitize(html)

    val regex = Regex("<img[^>]+src=\"([^\">]+)")

    val result: MatchResult? = regex.find(imageTags)

    return try {
        result?.groups?.get(1)?.value ?: ""
    } catch (_: Exception) {
        ""
    }
}
