package com.yellowsunn.userservice.application.command;

import lombok.Builder;

@Builder
public record UserInfoUpdateCommand(
        Long userId,
        String nickName
) {

}
