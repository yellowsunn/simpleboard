package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.command.message.producer.ArticleMessageProducer
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.query.repository.ArticleViewCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ArticleViewCountSyncService(
    private val articleRepository: ArticleRepository,
    private val articleViewCacheRepository: ArticleViewCacheRepository,
    private val articleMessageProducer: ArticleMessageProducer,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun updateArticleViewCount() {
        val articleIds = articleViewCacheRepository.findArticleIds()
        articleIds.forEach {
            val viewCount = articleViewCacheRepository.popViewCount(it)
            try {
                articleRepository.updateViewCount(it, viewCount)
                articleMessageProducer.syncArticleDocument(it)
            } catch (e: Exception) {
                logger.info("Failed to update article view count. articleId={}", it)
                articleViewCacheRepository.increaseViewCount(it, viewCount)
            }
        }
    }
}
