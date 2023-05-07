package com.yellowsunn.userservice.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class BCryptPasswordEncoder implements PasswordEncoder {
    private static final int COST = 6;

    @Override
    public String encode(String rawPassword) {
        Assert.notNull(rawPassword, "rawPassword must not be null");
        return BCrypt.withDefaults().hashToString(COST, rawPassword.toCharArray());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        Assert.notNull(rawPassword, "rawPassword must not be null");
        Assert.notNull(encodedPassword, "encodedPassword must not be null");

        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);

        return result.verified;
    }
}
