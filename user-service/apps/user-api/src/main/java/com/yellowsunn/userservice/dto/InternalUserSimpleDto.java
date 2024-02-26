package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.UserEntity;
import lombok.Builder;

@Builder
public record InternalUserSimpleDto(
        String userId,
        String nickName,
        String thumbnail
) {

    public static InternalUserSimpleDto from(User user) {
        return InternalUserSimpleDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .build();
    }
}
