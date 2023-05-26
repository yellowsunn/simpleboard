package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.constant.OAuth2Type;
import lombok.Builder;

@Builder
public record UserOAuth2LinkCommand(
        String userUUID,
        String oAuth2Token,
        OAuth2Type type,
        String state
) {
}
