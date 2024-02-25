package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.User;
import java.util.List;
import lombok.Builder;

@Builder
public record UserMyInfoDto(String email, String nickName, String thumbnail, List<Provider> providers) {

    public static UserMyInfoDto fromUser(User user, List<Provider> providers) {
        return UserMyInfoDto.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .providers(providers)
                .build();
    }
}
