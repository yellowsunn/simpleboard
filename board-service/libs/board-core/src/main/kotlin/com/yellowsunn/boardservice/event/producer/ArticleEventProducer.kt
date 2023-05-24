package com.yellowsunn.boardservice.event.producer

import com.yellowsunn.boardservice.event.ArticleLikeSyncEvent
import com.yellowsunn.boardservice.event.ArticleSyncEvent

interface ArticleEventProducer {
    fun syncArticleEvent(event: ArticleSyncEvent)
    fun syncArticleLikeEvent(event: ArticleLikeSyncEvent)
}
