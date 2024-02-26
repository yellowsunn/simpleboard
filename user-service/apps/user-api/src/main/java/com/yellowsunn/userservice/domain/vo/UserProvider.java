package com.yellowsunn.userservice.domain.vo;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.dto.UserProviderCreate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@EqualsAndHashCode
public class UserProvider {

    private final String email;
    private final Provider provider;

    @Builder
    public UserProvider(String email, Provider provider) {
        Assert.hasText(email, () -> "email must not be blank.");
        this.email = email;

        Assert.notNull(provider, () -> "provider must not be null");
        this.provider = provider;
    }

    public static UserProvider createEmailProvider(String email) {
        return new UserProvider(email, Provider.EMAIL);
    }

    public static UserProvider createOAuth2Provider(String email, Provider provider) {
        Provider.checkOAuth2(provider);
        return new UserProvider(email, provider);
    }

    public UserProviderCreate toUserProviderCreate(String userId) {
        return UserProviderCreate.builder()
                .userId(userId)
                .email(email)
                .provider(provider)
                .build();
    }
}
