package com.yellowsunn.userservice.domain.vo;

import org.springframework.util.Assert;

public final class EmailProvider implements UserProvider {

    private final String email;

    private EmailProvider(String email) {
        Assert.hasText(email, () -> "email must not be blank.");
        this.email = email;
    }

    public static EmailProvider fromEmail(String email) {
        return new EmailProvider(email);
    }
}
