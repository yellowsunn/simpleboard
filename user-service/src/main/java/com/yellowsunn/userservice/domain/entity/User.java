package com.yellowsunn.userservice.domain.entity;

import com.yellowsunn.userservice.domain.dto.UserCreate;
import com.yellowsunn.userservice.domain.vo.EmailProvider;
import com.yellowsunn.userservice.domain.vo.UserId;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import java.util.List;
import lombok.Builder;

public final class User {

    private UserId userId;
    private String email;
    private String password;
    private String nickname;
    private List<UserProvider> providers;

    @Builder
    private User(UserId userId, String email, String password, String nickname, List<UserProvider> providers) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.providers = providers;
    }

    public static User createEmailUser(String userId, String email, String password, String nickname) {
        return User.builder()
                .userId(UserId.fromString(userId))
                .email(email)
                .password(password)
                .nickname(nickname)
                .providers(List.of(EmailProvider.fromEmail(email)))
                .build();
    }

    public UserCreate toUserCreate() {
        return UserCreate.builder()
                .userId(userId.toString())
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
