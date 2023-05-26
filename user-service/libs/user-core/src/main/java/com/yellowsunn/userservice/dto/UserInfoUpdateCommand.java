package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserInfoUpdateCommand(
        String userUUID,
        String nickName
) {
}
