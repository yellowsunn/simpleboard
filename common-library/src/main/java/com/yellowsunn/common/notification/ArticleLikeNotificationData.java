package com.yellowsunn.common.notification;

public record ArticleLikeNotificationData(
        Long articleId
) implements NotificationData {
}
