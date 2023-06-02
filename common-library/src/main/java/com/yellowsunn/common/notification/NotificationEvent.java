package com.yellowsunn.common.notification;

public record NotificationEvent(
        Long userId,
        String uuid,
        String tag,
        String title,
        String content,
        String contentLink
) {
}
