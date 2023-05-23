package com.yellowsunn.boardservice.event.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ArticleViewEventListener(
) {
    @EventListener
    fun increaseViewCount(articleViewEvent: ArticleViewEvent) {

    }
}
