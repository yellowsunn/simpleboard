package com.yellowsunn.userservice.application.command;

import lombok.Builder;

@Builder
public record UserOAuth2SignUpCommand(
        String tempUserToken,
        String csrfToken,
        String nickName
) {

}
