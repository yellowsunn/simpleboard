package com.yellowsunn.boardservice.event.listener

import com.yellowsunn.boardservice.command.domain.comment.Comment
import com.yellowsunn.boardservice.command.message.producer.ArticleMessageProducer
import com.yellowsunn.boardservice.command.message.producer.CommentMessageProducer
import com.yellowsunn.boardservice.command.message.producer.NotificationMessageProducer
import com.yellowsunn.boardservice.command.message.producer.data.notification.NotificationMessage
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.command.repository.CommentRepository
import com.yellowsunn.boardservice.command.repository.RateLimitCacheRepository
import com.yellowsunn.boardservice.event.data.comment.CommentDeleteEvent
import com.yellowsunn.boardservice.event.data.comment.CommentLikeEvent
import com.yellowsunn.boardservice.event.data.comment.CommentSaveEvent
import com.yellowsunn.boardservice.event.data.comment.CommentUndoLikeEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CommentEventListener(
    private val commentMessageProducer: CommentMessageProducer,
    private val articleMessageProducer: ArticleMessageProducer,
    private val notificationMessageProducer: NotificationMessageProducer,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val rateLimitCacheRepository: RateLimitCacheRepository,
) {
    @Async
    @EventListener
    fun saveComment(event: CommentSaveEvent) {
        commentMessageProducer.syncCommentDocument(event.commentId)
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun notifySaveComment(event: CommentSaveEvent) {
        val userId: Long? = getCommentBaseUserId(event)

        // 사용자를 찾을 수 없거나, 동일 작성자에게는 전송하지 않음
        if (userId == null || userId == event.userId) {
            return
        }

        notificationMessageProducer.notify(
            NotificationMessage.buildCommentMessage(
                commentId = event.commentId,
                articleId = event.articleId,
                userId = userId,
                content = event.content,
                isReply = event.isReply,
            ),
        )
    }

    @Async
    @EventListener
    fun deleteComment(event: CommentDeleteEvent) {
        commentMessageProducer.syncCommentDocument(event.commentId)
        articleMessageProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun likeComment(event: CommentLikeEvent) {
        commentMessageProducer.syncCommentDocument(event.commentId)
        articleMessageProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }

    @Async
    @EventListener
    fun notifyLikeComment(event: CommentLikeEvent) {
        val comment: Comment = commentRepository.findById(event.commentId) ?: return
        if (comment.userId == event.userId) {
            return
        }

        // 1분동안 최초 한번의 요청만 전달
        val isAcquired: Boolean = rateLimitCacheRepository.acquireJustOne(
            "userId:${comment.userId}:commentId:${comment.id}",
            Duration.ofMinutes(1L),
        )
        if (isAcquired.not()) {
            return
        }

        val notificationMessage = NotificationMessage.buildCommentLikeMessage(
            commentId = comment.id,
            articleId = comment.articleId,
            userId = comment.userId,
            content = comment.content ?: "",
            imageUrl = comment.imageUrl ?: "",
        )
        notificationMessageProducer.notify(notificationMessage)
    }

    @Async
    @EventListener
    fun undoLikeComment(event: CommentUndoLikeEvent) {
        commentMessageProducer.syncCommentDocument(event.commentId)
        articleMessageProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }

    private fun getCommentBaseUserId(event: CommentSaveEvent): Long? {
        return if (event.isReply) {
            // 답글일 경우 기준 댓글 사용자 ID 반환
            val parentCommentId: Long? = commentRepository.findById(event.commentId)?.parentCommentId
            parentCommentId?.let {
                commentRepository.findById(it)
            }?.userId
        } else {
            // 댓글일 경우 게시글 작성자 ID 반환
            articleRepository.findById(event.articleId)?.userId
        }
    }
}
