package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.command.article.ArticleLike
import com.yellowsunn.boardservice.domain.command.article.ArticleLikeId
import com.yellowsunn.boardservice.dto.ArticleUpdateCommand
import com.yellowsunn.boardservice.exception.ArticleNotFoundException
import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import jakarta.persistence.EntityExistsException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleCommandService(
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
) {
    @Transactional
    fun saveArticle(userId: Long, title: String, body: String): Article {
        val article = Article(
            title = title,
            body = body,
            userId = userId,
        )

        return articleRepository.save(article)
    }

    @Transactional
    fun updateArticle(userId: Long, command: ArticleUpdateCommand): Boolean {
        val article: Article = articleRepository.findById(command.articleId)
            ?: throw ArticleNotFoundException()

        if (article.id != command.articleId) {
            throw IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.")
        }

        return article.updateTitleAndBody(command.title, command.body)
    }

    fun likeArticle(userId: Long, articleId: Long): Boolean {
        checkValidArticleId(articleId)

        val articleLike = ArticleLike(
            articleId = articleId,
            userId = userId,
        )

        return try {
            articleLikeRepository.save(articleLike)
            true
        } catch (_: EntityExistsException) {
            false
        } catch (_: DataIntegrityViolationException) {
            false
        }
    }

    @Transactional
    fun undoLikeArticle(userId: Long, articleId: Long): Boolean {
        checkValidArticleId(articleId)

        val id = ArticleLikeId(
            articleId = articleId,
            userId = userId,
        )
        return articleLikeRepository.deleteById(id)
    }

    private fun checkValidArticleId(articleId: Long) {
        articleRepository.findById(articleId)
            ?: throw ArticleNotFoundException()
    }
}
