package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OAuth2LinkUserRequestDto {
    @NotBlank
    private String tempUserToken;
}
