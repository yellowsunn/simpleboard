package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserLoginTokenDto(String accessToken, String refreshToken) {
}
