package com.yellowsunn.userservice.presentation.request;

import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.vo.UserId;

public record EmailSignUpRequest(
        String email,
        String password,
        String nickname
) {

    public UserCreateCommand toUserCreateCommand(UserId userId) {
        return UserCreateCommand.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
