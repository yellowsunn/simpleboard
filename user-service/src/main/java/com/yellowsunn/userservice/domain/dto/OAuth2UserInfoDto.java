package com.yellowsunn.userservice.domain.dto;

import lombok.Builder;

@Builder
public record OAuth2UserInfoDto(
        String email,
        String thumbnail
) {

}
