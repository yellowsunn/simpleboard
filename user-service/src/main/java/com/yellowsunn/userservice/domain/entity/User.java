package com.yellowsunn.userservice.domain.entity;

import com.yellowsunn.userservice.domain.dto.UserCreate;
import com.yellowsunn.userservice.domain.dto.UserProviderCreate;
import com.yellowsunn.userservice.domain.entity.provider.EmailProvider;
import com.yellowsunn.userservice.domain.entity.provider.UserProvider;
import com.yellowsunn.userservice.domain.vo.UserId;
import java.util.List;
import lombok.Builder;
import org.springframework.util.Assert;

public final class User {

    private final UserId userId;
    private final String nickname;
    private final List<UserProvider> providers;

    @Builder
    private User(UserId userId, String nickname, List<UserProvider> providers) {
        Assert.notNull(userId, () -> "userId must not be null.");
        this.userId = userId;

        Assert.hasText(nickname, () -> "nickname must not be blank.");
        this.nickname = nickname;

        Assert.notEmpty(providers, () -> "providers must not be empty.");
        this.providers = providers;
    }

    public static User createEmailUser(UserId userId, String email, String nickname, String password) {
        return User.builder()
                .userId(userId)
                .nickname(nickname)
                .providers(List.of(EmailProvider.from(userId, email, password)))
                .build();
    }

    public UserCreate toUserCreate() {
        return UserCreate.builder()
                .userId(userId.toString())
                .nickname(nickname)
                .build();
    }

    public List<UserProviderCreate> toUserProviderCreates() {
        return providers.stream()
                .map(UserProvider::toUserProviderCreate)
                .toList();
    }

}
