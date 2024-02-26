package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import java.util.List;
import lombok.Builder;

@Builder
public record UserMyInfoDto(
        String email,
        String nickName,
        String thumbnail,
        List<Provider> providers
) {

    public static UserMyInfoDto from(User user) {
        return UserMyInfoDto.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .providers(user.providers())
                .build();
    }
}
