package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.UserRole;
import lombok.Builder;

@Builder
public record UserCreate(
        String nickName,
        String email,
        String password,
        String userId,
        String thumbnail,
        UserRole role
) {

}
