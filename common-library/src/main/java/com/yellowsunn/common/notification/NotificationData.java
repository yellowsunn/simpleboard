package com.yellowsunn.common.notification;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = CommentNotificationData.class, name = "comment")
})
public interface NotificationData {
}
