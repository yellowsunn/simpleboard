package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OAuth2SignUpRequestDto {
    @NotBlank
    private String tempUserToken;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$", message = "사용할 수 없는 닉네임입니다.")
    private String nickName;

    public UserOAuth2SignUpCommand toUserOAuth2SignUpCommand() {
        return UserOAuth2SignUpCommand.builder()
                .tempUserToken(tempUserToken)
                .nickName(nickName)
                .build();
    }
}
