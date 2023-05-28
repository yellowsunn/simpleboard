package com.yellowsunn.boardservice.command.service

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.command.domain.comment.Comment
import com.yellowsunn.boardservice.command.domain.comment.CommentLike
import com.yellowsunn.boardservice.command.domain.comment.CommentLikeId
import com.yellowsunn.boardservice.command.dto.CommentLikeDto
import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.command.repository.CommentLikeRepository
import com.yellowsunn.boardservice.command.repository.CommentRepository
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.exception.ArticleNotFoundException
import com.yellowsunn.boardservice.common.exception.CommentNotFoundException
import jakarta.persistence.EntityExistsException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentCommandService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
) {

    @Transactional
    fun saveComment(user: User, command: CommentSaveCommand): CommentSavedDto {
        val article: Article = articleRepository.findById(command.articleId)
            ?: throw ArticleNotFoundException()

        val comment = Comment(
            articleId = article.id,
            userId = command.userId,
            content = command.content,
            imageUrl = command.imageUrl,
            parentCommentId = command.parentCommentId,
        )
        val savedComment = commentRepository.save(comment)
        savedComment.changeBaseCommentId(getBaseCommentId(savedComment))

        return CommentSavedDto.from(savedComment, user.nickName, user.thumbnail)
    }

    fun likeComment(userId: Long, commentId: Long): CommentLikeDto? {
        val comment = commentRepository.findById(commentId)
            ?: throw CommentNotFoundException()

        val commentLike = CommentLike(
            commentId = comment.id,
            userId = userId,
            articleId = comment.articleId,
        )

        return try {
            val savedCommentLike = commentLikeRepository.save(commentLike)
            CommentLikeDto(
                articleId = savedCommentLike.articleId,
                commentId = savedCommentLike.commentId,
            )
        } catch (_: EntityExistsException) {
            null
        } catch (_: DataIntegrityViolationException) {
            null
        }
    }

    @Transactional
    fun undoLikeComment(userId: Long, commentId: Long): CommentLikeDto? {
        val comment = commentRepository.findById(commentId)
            ?: throw CommentNotFoundException()

        val id = CommentLikeId(
            commentId = comment.id,
            userId = userId,
        )

        val isDeleted: Boolean = commentLikeRepository.deleteById(id)
        return if (isDeleted) {
            CommentLikeDto(
                articleId = comment.articleId,
                commentId = comment.id,
            )
        } else {
            null
        }
    }

    private fun getBaseCommentId(comment: Comment): Long {
        return if (comment.parentCommentId != null) {
            // 답글인 경우
            val parentComment: Comment = commentRepository.findById(comment.parentCommentId)
                ?: throw IllegalArgumentException("댓글을 찾을 수 없습니다.")
            parentComment.baseCommentId
        } else {
            // 댓글인 경우
            comment.id
        }
    }
}
