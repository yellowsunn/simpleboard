package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.query.article.ArticleDocument
import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.repository.article.ArticleQueryRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import kotlin.math.max
import kotlin.math.min
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ArticleQueryService(
    private val articleRepository: ArticleRepository,
    private val articleQueryRepository: ArticleQueryRepository,
) {
    private companion object {
        private const val DEFAULT_ELEM_SIZE = 10
        private const val MAX_ELEM_SIZE = 100
    }

    fun sync(articleId: Long): Boolean {
        val article: Article = articleRepository.findById(articleId) ?: return false

        val articleDocument = ArticleDocument(
            articleId = articleId,
            title = article.title,
            body = article.body,
            readCount = article.readCount,
            likeCount = article.readCount,
            userId = article.userId,
            savedAt = article.createdAt,
        )
        articleQueryRepository.save(articleDocument)
        return true
    }

    fun findArticleByDocumentId(id: String): ArticleDocumentDto {
        val articleDocument = articleQueryRepository.findById(id)
            ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

        return ArticleDocumentDto.from(articleDocument)
    }

    fun findArticles(page: Int, size: Int): ArticleDocumentPageDto {
        val curPage = max(page, 1) - 1
        val curSize = if (size <= 0) {
            DEFAULT_ELEM_SIZE
        } else {
            min(size, MAX_ELEM_SIZE)
        }

        val articleDocumentPage: Page<ArticleDocument> = articleQueryRepository.findArticles(curPage, curSize)
        return ArticleDocumentPageDto.from(articleDocumentPage)
    }
}
