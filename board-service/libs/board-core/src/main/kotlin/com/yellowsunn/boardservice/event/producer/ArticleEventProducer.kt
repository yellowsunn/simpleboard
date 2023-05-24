package com.yellowsunn.boardservice.event.producer

interface ArticleEventProducer {
    fun saveArticleEvent(articleId: Long)
    fun updateArticleLikeEvent(articleId: Long)
    fun updateArticleUndoLikeEvent(articleId: Long)
}
