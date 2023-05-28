package com.yellowsunn.boardservice.command.service

import com.yellowsunn.boardservice.command.domain.article.Article
import com.yellowsunn.boardservice.command.domain.comment.Comment
import com.yellowsunn.boardservice.command.dto.CommentSaveCommand
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.command.repository.CommentRepository
import com.yellowsunn.boardservice.common.domain.user.User
import com.yellowsunn.boardservice.common.exception.ArticleNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentCommandService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
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
