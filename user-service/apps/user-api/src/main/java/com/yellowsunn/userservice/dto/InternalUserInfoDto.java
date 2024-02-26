package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.vo.UserProvider;
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
                .email(user.getEmail())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .providers(user.getUserProviders().stream()
                        .map(UserProvider::getProvider)
                        .toList()
                ).build();
    }
}
