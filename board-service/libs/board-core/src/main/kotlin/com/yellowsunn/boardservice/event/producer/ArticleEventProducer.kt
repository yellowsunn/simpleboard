package com.yellowsunn.boardservice.event.producer

import com.yellowsunn.boardservice.event.ArticleCreateEvent
import com.yellowsunn.boardservice.event.ArticleLikeEvent
import com.yellowsunn.boardservice.event.ArticleUndoLikeEvent
import com.yellowsunn.boardservice.event.ArticleUpdateEvent

interface ArticleEventProducer {
    fun saveArticleEvent(event: ArticleCreateEvent)
    fun updateArticleEvent(event: ArticleUpdateEvent)
    fun updateArticleLikeEvent(event: ArticleLikeEvent)
    fun updateArticleUndoLikeEvent(event: ArticleUndoLikeEvent)
}
