package com.yellowsunn.boardservice.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HtmlSanitizersTest {

    @Test
    fun sanitizeHtmlTest() {
        val html = "<script>alert('hi')</script><p>hi</p>"

        val result = sanitizeHtml(html)

        assertThat(result).isEqualTo("<p>hi</p>")
    }

    @Test
    fun getFirstImageSrc() {
        val html = "<p><img src='http://example1.com'></p><p><img src='http://example2.com'></p>"

        val result = getFirstImageSrc(html)

        assertThat(result).isEqualTo("http://example1.com")
    }
}
