package com.yellowsunn.userservice.domain.dto;

import com.yellowsunn.userservice.domain.vo.UserId;
import lombok.Builder;

@Builder
public record UserCreateCommand(
        UserId userId,
        String email,
        String password,
        String nickname
) {

}
