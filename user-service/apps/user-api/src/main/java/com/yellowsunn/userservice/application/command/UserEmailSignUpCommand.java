package com.yellowsunn.userservice.application.command;

import lombok.Builder;

@Builder
public record UserEmailSignUpCommand(
        String email,
        String password,
        String nickName
) {

}
