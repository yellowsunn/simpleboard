package com.yellowsunn.userservice.domain.dto;

import lombok.Builder;

@Builder
public record UserCreate(
        String userId,
        String email,
        String password,
        String nickname,
        String thumbnail
) {

}
