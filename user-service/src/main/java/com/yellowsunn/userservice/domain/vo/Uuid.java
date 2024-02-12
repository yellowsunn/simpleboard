package com.yellowsunn.userservice.domain.vo;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

public record Uuid(
        String value
) {

    public Uuid {
        Pattern pattern = Pattern.compile(
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        );

        Assert.isTrue(pattern.matcher(value).matches(), () -> "잘못된 UUID 형식입니다. value=%s".formatted(value));
    }

    public fromString() {

    }
}
