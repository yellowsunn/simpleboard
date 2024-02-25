package com.yellowsunn.userservice.application.command;

import lombok.Builder;

@Builder
public record UserEmailLoginCommand(
        String email,
        String password
) {

}
