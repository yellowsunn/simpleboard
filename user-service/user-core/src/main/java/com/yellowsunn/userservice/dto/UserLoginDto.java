package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserLoginDto(String accessToken, String refreshToken) {
}
