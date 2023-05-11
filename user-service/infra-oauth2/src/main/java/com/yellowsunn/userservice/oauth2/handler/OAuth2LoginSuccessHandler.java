package com.yellowsunn.userservice.oauth2.handler;

import com.yellowsunn.userservice.oauth2.CustomOAuth2User;
import com.yellowsunn.userservice.utils.token.AccessTokenGenerator;
import com.yellowsunn.userservice.utils.token.AccessTokenPayload;
import com.yellowsunn.userservice.utils.token.RefreshTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final String redirectOrigin;

    public OAuth2LoginSuccessHandler(AccessTokenGenerator accessTokenGenerator,
                                     RefreshTokenGenerator refreshTokenGenerator,
                                     @Value("${redirect.origin}") String redirectOrigin) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.redirectOrigin = redirectOrigin;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String uri = generateUri(customOAuth2User);

        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String generateUri(CustomOAuth2User customOAuth2User) {
        if (customOAuth2User.isExist()) {
            return generateTokenUri(customOAuth2User);
        }
        return generateOAuth2SignUpUri(customOAuth2User.getTempUserToken());
    }

    private String generateTokenUri(CustomOAuth2User customOAuth2User) {
        var accessTokenPayload = new AccessTokenPayload(customOAuth2User.getUuid(), customOAuth2User.getEmail());

        return UriComponentsBuilder.fromUriString(redirectOrigin)
                .path("/token")
                .queryParam("access", accessTokenGenerator.generateEncodedToken(accessTokenPayload))
                .queryParam("refresh", refreshTokenGenerator.generateEncodedToken())
                .build().toUriString();
    }

    private String generateOAuth2SignUpUri(String tempUserToken) {
        return UriComponentsBuilder.fromUriString(redirectOrigin)
                .path("/oauth2/signup")
                .queryParam("token", tempUserToken)
                .build().toUriString();
    }
}
