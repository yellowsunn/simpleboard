package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.Provider;
import lombok.Builder;

@Builder
public record UserProviderCreate(
        String userProviderId,
        String userId,
        String email,
        Provider provider
) {

}
