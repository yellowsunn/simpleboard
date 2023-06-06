package com.yellowsunn.common.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationDataTest {
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void serializeAndDeserializeTest() throws JsonProcessingException {
        var commentNotificationData = new CommentNotificationData(1L, 1L);

        String serializedData = objectMapper.writeValueAsString(commentNotificationData);
        NotificationData notificationData = objectMapper.readValue(serializedData, NotificationData.class);

        assertThat(notificationData).isInstanceOf(CommentNotificationData.class);
    }
}
