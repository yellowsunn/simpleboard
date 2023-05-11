package com.yellowsunn.userservice.oauth2.service;

import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.oauth2.CustomOAuth2User;
import com.yellowsunn.userservice.oauth2.OAuth2Attribute;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate;
    private final UserProviderRepository userProviderRepository;
    private final TempUserCacheRepository tempUserCacheRepository;

    private final Duration tempUserDuration = Duration.ofMinutes(30);

    public CustomOAuth2UserService(UserProviderRepository userProviderRepository,
                                   TempUserCacheRepository tempUserCacheRepository) {
        this.userProviderRepository = userProviderRepository;
        this.tempUserCacheRepository = tempUserCacheRepository;
        this.delegate = new DefaultOAuth2UserService();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = getRegistrationId(userRequest);
        String userNameAttributeName = getUserNameAttributeName(userRequest);

        var attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Optional<UserProvider> userProviderOptional =
                userProviderRepository.findByProviderEmailAndProvider(attribute.getEmail(), attribute.getProvider());

        // 이미 존재하는 회원이면 회원정보를 반환
        if (userProviderOptional.isPresent()) {
            return CustomOAuth2User.existUserBuilder()
                    .oAuth2Attribute(attribute)
                    .user(userProviderOptional.get().getUser())
                    .build();
        } else {
            TempUser tempUser = saveTempUser(attribute);
            return CustomOAuth2User.tempUserBuilder()
                    .oAuth2Attribute(attribute)
                    .tempUserToken(tempUser.getToken())
                    .build();
        }
    }

    private String getRegistrationId(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration().getRegistrationId();
    }

    private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    }

    private TempUser saveTempUser(OAuth2Attribute attribute) {
        var tempUser = TempUser.builder()
                .email(attribute.getEmail())
                .provider(attribute.getProvider())
                .thumbnail(attribute.getPicture())
                .build();
        tempUserCacheRepository.save(tempUser, tempUserDuration);

        return tempUser;
    }
}
