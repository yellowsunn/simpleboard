package com.yellowsunn.boardservice.command.event.listener

import com.yellowsunn.boardservice.command.event.data.comment.CommentDeleteEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentLikeEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentSaveEvent
import com.yellowsunn.boardservice.command.event.data.comment.CommentUndoLikeEvent
import com.yellowsunn.boardservice.command.event.producer.ArticleEventProducer
import com.yellowsunn.boardservice.command.event.producer.CommentEventProducer
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CommentEventListener(
    private val commentEventProducer: CommentEventProducer,
    private val articleEventProducer: ArticleEventProducer,
) {
    @EventListener
    fun saveComment(event: CommentSaveEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun deleteComment(event: CommentDeleteEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleDocument(event.articleId)
    }

    @EventListener
    fun likeComment(event: CommentLikeEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }

    @EventListener
    fun undoLikeComment(event: CommentUndoLikeEvent) {
        commentEventProducer.syncCommentDocument(event.commentId)
        articleEventProducer.syncArticleReactionDocument(
            articleId = event.articleId,
            userId = event.userId,
        )
    }
}
