package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserLoginCommand(String email, String password) {
}
