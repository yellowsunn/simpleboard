package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserEmailSignUpCommand(
        String email,
        String password,
        String nickName
) {
}
