package com.yellowsunn.common.notification;


public record CommentNotificationData(
        Long commentId,
        Long articleId
) implements NotificationData {
}
