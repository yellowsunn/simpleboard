package com.yellowsunn.userservice.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class BCryptPasswordEncoder implements PasswordEncoder {

    private static final int COST = 6;

    @Override
    public String encode(String rawPassword) {
        Assert.hasText(rawPassword, () -> "rawPassword must not be blank");

        return BCrypt.withDefaults().hashToString(COST, rawPassword.toCharArray());
    }
}
