package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.data.comment.CommentDeleteEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentLikeEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentSaveEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentUndoLikeEvent
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.CommentEventProducer
import com.yellowsunn.boardservice.command.event.producer.NotificationMessageProducer
import com.yellowsunn.boardservice.command.event.producer.data.notification.NotificationMessage
import com.yellowsunn.boardservice.command.repository.ArticleRepository
import com.yellowsunn.boardservice.command.repository.CommentRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CommentEventListener(
    private val commentEventProducer: CommentEventProducer,
    private val articleEventProducer: ArticleEventProducer,
    private val notificationMessageProducer: NotificationMessageProducer,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
) {
    @Async
    @EventListener
    fun saveComment(event: CommentSaveEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun notifySaveComment(event: CommentSaveEvent) {
        val userId: Long? = if (event.isReply) {
            // 답글일 경우 기준 댓글 사용자 ID 반환
            val parentCommentId: Long? = commentRepository.findById(event.commentId)?.parentCommentId
            parentCommentId?.let {
                commentRepository.findById(it)
            }?.userId
        } else {
            // 댓글일 경우 게시글 작성자 ID 반환
            articleRepository.findById(event.articleId)?.userId
        }

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
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @Async
    @EventListener
    fun likeComment(event: CommentLikeEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }

    @Async
    @EventListener
    fun undoLikeComment(event: CommentUndoLikeEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }
}
