package com.yellowsunn.userservice.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record UserUpdate(
        String nickName,
        String password,
        String thumbnail,
        List<UserProviderCreate> addedUserProviders
) {

}
