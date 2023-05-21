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
}
