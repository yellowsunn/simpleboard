package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserEmailLoginCommand(String email, String password) {
}
