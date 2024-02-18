package com.yellowsunn.userservice.domain.port;

import com.yellowsunn.userservice.domain.dto.OAuth2UserInfoDto;
import java.util.Optional;

public interface OAuth2HttpClient {

    Optional<OAuth2UserInfoDto> findGoogleUserInfo(String token);

    Optional<OAuth2UserInfoDto> findNaverUserInfo(String token);

    Optional<OAuth2UserInfoDto> findKakaoUserInfo(String code);
}
