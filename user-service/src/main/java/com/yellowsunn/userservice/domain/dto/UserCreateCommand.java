package com.yellowsunn.userservice.domain.dto;

import lombok.Builder;

@Builder
public record UserCreateCommand(
        String userId,
        String email,
        String password,
        String nickname
) {

}
