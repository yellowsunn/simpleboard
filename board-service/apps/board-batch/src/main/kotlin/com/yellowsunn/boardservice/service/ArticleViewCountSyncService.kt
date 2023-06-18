package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.command.message.producer.ArticleMessageProducer
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.query.repository.ArticleViewCacheRepository
import org.springframework.stereotype.Service

@Service
class ArticleViewCountSyncService(
    private val articleRepository: ArticleRepository,
    private val articleViewCacheRepository: ArticleViewCacheRepository,
    private val articleMessageProducer: ArticleMessageProducer,
) {
    fun updateArticleViewCount() {
        val articleIds = articleViewCacheRepository.findArticleIds()
        articleIds.forEach {
            val viewCount = articleViewCacheRepository.popViewCount(it)
            articleRepository.updateViewCount(it, viewCount)
            articleMessageProducer.syncArticleDocument(it)
        }
    }
}
