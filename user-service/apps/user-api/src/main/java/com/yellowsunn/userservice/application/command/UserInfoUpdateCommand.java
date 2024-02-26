package com.yellowsunn.userservice.application.command;

import lombok.Builder;

@Builder
public record UserInfoUpdateCommand(
        String userId,
        String nickName
) {

}
