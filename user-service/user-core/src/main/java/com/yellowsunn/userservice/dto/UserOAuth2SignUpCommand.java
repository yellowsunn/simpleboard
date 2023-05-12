package com.yellowsunn.userservice.dto;

import lombok.Builder;

@Builder
public record UserOAuth2SignUpCommand(
        String tempUserToken,
        String nickName
) {
}
