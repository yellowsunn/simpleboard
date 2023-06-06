package com.yellowsunn.common.notification;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = CommentNotificationData.class, name = "comment"),
        @JsonSubTypes.Type(value = ArticleLikeNotificationData.class, name = "articleLike"),
        @JsonSubTypes.Type(value = CommentLikeNotificationData.class, name = "commentLike")
})
public interface NotificationData {
}
