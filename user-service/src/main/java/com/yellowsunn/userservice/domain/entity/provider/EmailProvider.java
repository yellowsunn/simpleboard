package com.yellowsunn.userservice.domain.entity.provider;

import com.yellowsunn.userservice.domain.dto.UserProviderCreate;
import com.yellowsunn.userservice.domain.vo.Provider;
import com.yellowsunn.userservice.domain.vo.UserId;
import org.springframework.util.Assert;

public final class EmailProvider implements UserProvider {

    private final UserId userId;
    private final String email;
    private final String password;

    private EmailProvider(UserId userId, String email, String password) {
        Assert.notNull(userId, () -> "userId must not be null.");
        this.userId = userId;

        Assert.hasText(email, () -> "email must not be blank.");
        this.email = email;

        Assert.hasText(password, () -> "password must not be blank.");
        this.password = password;
    }

    public static EmailProvider from(UserId userId, String email, String password) {
        return new EmailProvider(userId, email, password);
    }

    @Override
    public UserProviderCreate toUserProviderCreate() {
        return UserProviderCreate.builder()
                .userId(userId.toString())
                .provider(Provider.EMAIL)
                .email(email)
                .password(password)
                .build();
    }
}
