package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record InternalUserInfoDto(
        Long userId,
        String uuid,
        String email,
        String nickName,
        String thumbnail,
        List<Provider> providers
) {
    public static InternalUserInfoDto from(User user, List<Provider> providers) {
        return InternalUserInfoDto.builder()
                .userId(user.getId())
                .uuid(user.getUuid())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .thumbnail(user.getThumbnail())
                .providers(ListUtils.emptyIfNull(providers))
                .build();
    }
}
