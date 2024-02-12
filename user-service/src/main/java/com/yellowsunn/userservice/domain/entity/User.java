package com.yellowsunn.userservice.domain.entity;

import com.yellowsunn.userservice.domain.vo.Uuid;
import lombok.Builder;

public class User {

    private Uuid userId;
    private String email;
    private String password;
    private String nickname;

    @Builder
    private User(Uuid userId, String email, String password, String nickname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }


    public static User createUser(String userId, String email, String password, String nickname) {
        return User.builder()
                .userId(new Uuid(userId))
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
