package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class OAuth2SignUpRequestDto {
    private String state;

    @NotBlank
    private String tempUserToken;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$", message = "3~20자의 한글, 영어 소문자, 숫자 조합만 입력 가능합니다.")
    private String nickName;

    public UserOAuth2SignUpCommand toUserOAuth2SignUpCommand() {
        return UserOAuth2SignUpCommand.builder()
                .csrfToken(StringUtils.defaultString(state))
                .tempUserToken(tempUserToken)
                .nickName(nickName)
                .build();
    }
}
