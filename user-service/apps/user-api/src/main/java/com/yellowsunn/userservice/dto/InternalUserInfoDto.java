package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record InternalUserInfoDto(
        String userId,
        String email,
        String nickName,
        String thumbnail,
        List<Provider> providers
) {

    public static InternalUserInfoDto from(User user) {
        return InternalUserInfoDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .providers(user.providers())
                .build();
    }
}
