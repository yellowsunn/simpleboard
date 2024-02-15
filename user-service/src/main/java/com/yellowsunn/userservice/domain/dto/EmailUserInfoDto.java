package com.yellowsunn.userservice.domain.dto;

import lombok.Builder;

@Builder
public record EmailUserInfoDto(
        String userId,
        String password
) {

}
