package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserInfoUpdateCommand(
        Long userId,
        String nickName
) {
}
