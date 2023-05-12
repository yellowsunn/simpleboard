package com.yellowsunn.userservice.oauth2.handler;

import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.AccessTokenPayload;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import com.yellowsunn.userservice.oauth2.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.yellowsunn.userservice.oauth2.constant.SessionConst.OAUTH2_LINK_USER;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AccessTokenHandler accessTokenHandler;
    private final RefreshTokenHandler refreshTokenHandler;
    private final String frontEndUrl;

    public OAuth2LoginSuccessHandler(AccessTokenHandler accessTokenHandler,
                                     RefreshTokenHandler refreshTokenHandler,
                                     @Value("${micro-services.front-end-url}") String frontEndUrl) {
        this.accessTokenHandler = accessTokenHandler;
        this.refreshTokenHandler = refreshTokenHandler;
        this.frontEndUrl = frontEndUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        HttpSession session = request.getSession(false);
        String uri = generateUri(session, customOAuth2User);
        if (session != null) {
            session.invalidate();
        }
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String generateUri(HttpSession session, CustomOAuth2User customOAuth2User) {
        // 로그인
        if (customOAuth2User.isExist()) {
            return generateTokenUri(customOAuth2User);
        }
        // 기존 계정과 로그인 연동
        if (session != null && session.getAttribute(OAUTH2_LINK_USER) != null) {
            return generateOAuth2LinkUri(customOAuth2User.getTempUserToken(), session.getAttribute(OAUTH2_LINK_USER));
        }
        // 회원 가입
        return generateOAuth2SignUpUri(customOAuth2User.getTempUserToken());
    }

    // 로그인 성공
    private String generateTokenUri(CustomOAuth2User customOAuth2User) {
        var accessTokenPayload = new AccessTokenPayload(customOAuth2User.getUuid(), customOAuth2User.getEmail());

        return UriComponentsBuilder.fromUriString(frontEndUrl)
                .path("/token")
                .queryParam("access", accessTokenHandler.generateEncodedToken(accessTokenPayload))
                .queryParam("refresh", refreshTokenHandler.generateEncodedToken())
                .build().toUriString();
    }

    // 추가 정보 입력을 위한 회원가입 페이지로 이동
    private String generateOAuth2SignUpUri(String tempUserToken) {
        return UriComponentsBuilder.fromUriString(frontEndUrl)
                .path("/oauth2/signup")
                .queryParam("token", tempUserToken)
                .build().toUriString();
    }

    // 기존 계정과 연결 요청
    private String generateOAuth2LinkUri(String tempUserToken, Object uuid) {
        return UriComponentsBuilder.fromUriString(frontEndUrl)
                .path("/oauth2/link")
                .queryParam("token", tempUserToken)
                .queryParam("uuid", uuid)
                .build().toUriString();
    }
}
