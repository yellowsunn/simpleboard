package com.yellowsunn.userservice.infrastructure.encrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.yellowsunn.userservice.application.port.out.PasswordEncoder;
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

    @Override
    public boolean match(String rawPassword, String encodedPassword) {
        Assert.notNull(rawPassword, () -> "rawPassword must not be null");
        Assert.notNull(encodedPassword, () -> "encodedPassword must not be null");

        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);

        return result.verified;
    }
}
