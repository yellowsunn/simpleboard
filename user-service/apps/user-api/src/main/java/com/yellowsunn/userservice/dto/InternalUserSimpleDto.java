package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.User;
import lombok.Builder;

@Builder
public record InternalUserSimpleDto(
        Long userId,
        String uuid,
        String nickName,
        String thumbnail
) {

    public static InternalUserSimpleDto from(User user) {
        return InternalUserSimpleDto.builder()
                .userId(user.getId())
                .uuid(user.getUuid())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .build();
    }
}
