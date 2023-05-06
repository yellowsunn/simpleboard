package com.yellowsunn.userservice.dto;

import lombok.Getter;

@Getter
public class UserLoginDto {
    private String accessToken;
    private String refreshToken;
}
