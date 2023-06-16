package com.yellowsunn.boardservice.command.message.producer.data.notification

import com.yellowsunn.boardservice.command.message.producer.data.ProducerData
import com.yellowsunn.common.notification.ArticleLikeNotificationData
import com.yellowsunn.common.notification.CommentLikeNotificationData
import com.yellowsunn.common.notification.CommentNotificationData
import com.yellowsunn.common.notification.NotificationData

data class NotificationMessage(
    val userId: Long,
    val title: String,
    val content: String,
    val data: NotificationData,
) : ProducerData {
    companion object {
        fun buildCommentMessage(
            commentId: Long,
            articleId: Long,
            userId: Long,
            content: String,
            isReply: Boolean,
        ): NotificationMessage {
            val title = if (isReply) {
                "댓글에 새로운 답글이 달렸습니다."
            } else {
                "게시글에 새로운 댓글이 달렸습니다."
            }
            return NotificationMessage(
                userId = userId,
                title = title,
                content = content,
                data = CommentNotificationData(commentId, articleId),
            )
        }

        fun buildCommentLikeMessage(
            commentId: Long,
            articleId: Long,
            userId: Long,
            content: String,
            imageUrl: String,
        ): NotificationMessage {
            return NotificationMessage(
                userId = userId,
                title = "다른 사용자가 댓글에 좋아요를 표시했습니다.",
                content = content,
                data = CommentLikeNotificationData(commentId, articleId, imageUrl),
            )
        }

        fun buildArticleLikeMessage(
            articleId: Long,
            userId: Long,
            content: String,
        ): NotificationMessage {
            return NotificationMessage(
                userId = userId,
                title = "다른 사용자가 게시글에 좋아요를 표시했습니다.",
                content = content,
                data = ArticleLikeNotificationData(articleId),
            )
        }
    }
}
