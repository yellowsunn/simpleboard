package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.User;

public record UserMyInfoDto(String email, String nickName, String thumbnail) {
    public static UserMyInfoDto fromUser(User user) {
        return new UserMyInfoDto(user.getEmail(), user.getNickName(), user.getThumbnail());
    }
}
