package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.exception.ArticleNotFoundException
import com.yellowsunn.boardservice.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.repository.article.ArticleDocumentRepository
import com.yellowsunn.boardservice.repository.article.ArticleViewCacheRepository
import kotlin.math.max
import kotlin.math.min
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ArticleQueryService(
    private val articleDocumentRepository: ArticleDocumentRepository,
    private val articleViewCacheRepository: ArticleViewCacheRepository,
    private val userHttpClient: UserHttpClient,
) {
    private companion object {
        private const val DEFAULT_ELEM_SIZE = 10
        private const val MAX_ELEM_SIZE = 100
    }

    fun findArticleByDocumentId(id: String): ArticleDocumentDto {
        val articleDocument = articleDocumentRepository.findById(id)
            ?: throw ArticleNotFoundException()

        val users = userHttpClient.findUsersByIds(listOf(articleDocument.userId))
        // 조회수 증가
        val increasedViewCount = articleViewCacheRepository.increaseViewCount(articleDocument.articleId)

        return ArticleDocumentDto.from(articleDocument, users, increasedViewCount)
    }

    fun findArticles(page: Int, size: Int): ArticleDocumentPageDto {
        val curPage = max(page, 1) - 1
        val curSize = if (size <= 0) {
            DEFAULT_ELEM_SIZE
        } else {
            min(size, MAX_ELEM_SIZE)
        }

        val articleDocumentPage: Page<ArticleDocument> = articleDocumentRepository.findArticles(curPage, curSize)

        val users = userHttpClient.findUsersByIds(filterUserIds(articleDocumentPage))
        val viewCounts =
            articleViewCacheRepository.findViewCounts(filterArticleId(articleDocumentPage))

        return ArticleDocumentPageDto.from(articleDocumentPage, viewCounts, users)
    }

    private fun filterUserIds(articleDocumentPage: Page<ArticleDocument>): List<Long> {
        return articleDocumentPage.content.map { it.userId }
    }

    private fun filterArticleId(articleDocumentPage: Page<ArticleDocument>): List<Long> {
        return articleDocumentPage.content.map { it.articleId }
    }
}
