package com.yellowsunn.common.notification;

public record CommentLikeNotificationData(
        Long commentId,
        Long articleId,
        String imageUrl
) implements NotificationData {
}
