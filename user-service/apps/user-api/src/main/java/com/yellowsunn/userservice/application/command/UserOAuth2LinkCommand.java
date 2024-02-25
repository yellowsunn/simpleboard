package com.yellowsunn.userservice.application.command;

import com.yellowsunn.userservice.constant.OAuth2Type;
import lombok.Builder;

@Builder
public record UserOAuth2LinkCommand(
        Long userId,
        String oAuth2Token,
        OAuth2Type type,
        String state
) {

}
