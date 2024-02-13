package com.yellowsunn.userservice.domain.vo;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

@EqualsAndHashCode
public final class UserId {

    private final UUID id;

    private UserId(UUID id) {
        this.id = id;
    }

    public static UserId fromString(String userId) {
        Assert.hasText(userId, () -> "userId must not be blank.");
        return new UserId(UUID.fromString(userId));
    }
}
