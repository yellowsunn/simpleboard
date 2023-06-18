package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.common.exception.ArticleNotFoundException
import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.query.domain.article.ArticleDocument
import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.dto.ArticleReactionDocumentDto
import com.yellowsunn.boardservice.dto.UserArticleDocumentPageDto
import com.yellowsunn.boardservice.query.repository.ArticleDocumentRepository
import com.yellowsunn.boardservice.query.repository.ArticleReactionDocumentRepository
import com.yellowsunn.boardservice.query.repository.ArticleViewCacheRepository
import com.yellowsunn.common.utils.PageUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ArticleQueryService(
    private val articleDocumentRepository: ArticleDocumentRepository,
    private val articleReactionDocumentRepository: ArticleReactionDocumentRepository,
    private val articleViewCacheRepository: ArticleViewCacheRepository,
    private val userHttpClient: UserHttpClient,
) {
    private companion object {
        private const val MAX_ELEM_SIZE = 100
    }

    fun findArticleById(articleId: Long): ArticleDocumentDto {
        val articleDocument = articleDocumentRepository.findByArticleId(articleId)
            ?: throw ArticleNotFoundException()

        val users = userHttpClient.findUsersByIds(listOf(articleDocument.userId))
        // 조회수 증가
        val increasedViewCount = articleViewCacheRepository.increaseViewCount(articleDocument.articleId)

        return ArticleDocumentDto.from(articleDocument, users, increasedViewCount)
    }

    fun findArticles(page: Int, size: Int): ArticleDocumentPageDto {
        val curPage = PageUtils.currentPage(page - 1)
        val curSize = PageUtils.currentSize(size, MAX_ELEM_SIZE)

        val articleDocumentPage: Page<ArticleDocument> = articleDocumentRepository.findArticles(curPage, curSize)

        val users = userHttpClient.findUsersByIds(filterUserIds(articleDocumentPage))
        val viewCounts =
            articleViewCacheRepository.findViewCounts(filterArticleId(articleDocumentPage))

        return ArticleDocumentPageDto.from(articleDocumentPage, viewCounts, users)
    }

    fun findReactionByArticleId(articleId: Long, userId: Long): ArticleReactionDocumentDto? {
        val articleReactionDocument =
            articleReactionDocumentRepository.findByArticleIdAndUserId(articleId, userId) ?: return null

        return ArticleReactionDocumentDto.from(articleReactionDocument)
    }

    fun findUserArticles(userId: Long, page: Int, size: Int): UserArticleDocumentPageDto {
        val curPage = PageUtils.currentPage(page - 1)
        val curSize = PageUtils.currentSize(size, MAX_ELEM_SIZE)

        val articleDocumentPage: Page<ArticleDocument> =
            articleDocumentRepository.findUserArticles(userId, curPage, curSize)

        return UserArticleDocumentPageDto.from(articleDocumentPage)
    }

    private fun filterUserIds(articleDocumentPage: Page<ArticleDocument>): List<Long> {
        return articleDocumentPage.content.map { it.userId }
    }

    private fun filterArticleId(articleDocumentPage: Page<ArticleDocument>): List<Long> {
        return articleDocumentPage.content.map { it.articleId }
    }
}
