package com.yellowsunn.boardservice.service

import com.yellowsunn.boardservice.domain.command.article.Article
import com.yellowsunn.boardservice.domain.command.article.ArticleLike
import com.yellowsunn.boardservice.domain.command.article.ArticleLikeId
import com.yellowsunn.boardservice.repository.article.ArticleLikeRepository
import com.yellowsunn.boardservice.repository.article.ArticleRepository
import jakarta.persistence.EntityExistsException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
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

    fun likeArticle(userId: Long, articleId: Long): Boolean {
        articleRepository.findById(articleId)
            ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

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
        articleRepository.findById(articleId)
            ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

        val id = ArticleLikeId(
            articleId = articleId,
            userId = userId,
        )
        return articleLikeRepository.deleteById(id)
    }
}
