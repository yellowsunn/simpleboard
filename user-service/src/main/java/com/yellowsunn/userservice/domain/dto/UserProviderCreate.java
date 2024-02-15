package com.yellowsunn.userservice.domain.dto;

import com.yellowsunn.userservice.domain.vo.Provider;
import lombok.Builder;

@Builder
public record UserProviderCreate(
        String userId,
        Provider provider,
        String email,
        String password
) {

}
