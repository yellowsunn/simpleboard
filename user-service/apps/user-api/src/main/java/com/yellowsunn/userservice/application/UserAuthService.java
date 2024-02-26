package com.yellowsunn.userservice.application;

import com.yellowsunn.common.exception.ExpiredAccessTokenException;
import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.AccessTokenPayload;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import com.yellowsunn.userservice.application.command.UserEmailLoginCommand;
import com.yellowsunn.userservice.application.command.UserEmailSignUpCommand;
import com.yellowsunn.userservice.application.command.UserOAuth2SignUpCommand;
import com.yellowsunn.userservice.application.port.TempUserCacheRepository;
import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.infrastructure.http.oauth2.OAuth2UserInfo;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final TempUserCacheRepository tempUserCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenHandler accessTokenHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    private final UserRepository userRepository;

    private final Duration tempUserDuration = Duration.ofMinutes(30);

    @Transactional
    public void signUpEmail(UserEmailSignUpCommand command) {
        verifyAlreadyExistEmail(command.email(), Provider.EMAIL);
        verifyAlreadyExistNickName(command.nickName());

        String userId = UUID.randomUUID().toString();

        User emailUser = User.createEmailUser(userId, command.email(), command.nickName(),
                passwordEncoder.encode(command.password()));

        userRepository.insert(emailUser);
    }

    @Transactional
    public UserLoginTokenDto signUpOAuth2(UserOAuth2SignUpCommand command, TempUser tempUser) {
        verifyAlreadyExistEmail(tempUser.getEmail(), tempUser.getProvider());
        verifyAlreadyExistNickName(command.nickName());

        String userId = UUID.randomUUID().toString();

        User oAuth2User = User.createOAuth2User(userId, tempUser.getEmail(), command.nickName(),
                tempUser.getThumbnail(), tempUser.getProvider());

        userRepository.insert(oAuth2User);

        return generateUserToken(userId);
    }

    public UserLoginTokenDto loginEmail(UserEmailLoginCommand command) {
        User foundUser = userRepository.findByEmailAndProvider(command.email(), Provider.EMAIL)
                .filter(user -> passwordEncoder.matches(command.password(), user.getPassword()))
                .orElseThrow(() -> new CustomUserException(UserErrorCode.INVALID_LOGIN));

        return generateUserToken(foundUser.getUserId());
    }

    public UserLoginTokenDto loginOAuth2(String email, OAuth2Type type) {
        return userRepository.findByEmailAndProvider(email, type.toProvider())
                .map(it -> generateUserToken(it.getUserId()))
                .orElse(null);
    }

    @Transactional
    public String saveTempOAuth2User(OAuth2UserInfo userInfo, OAuth2Type type, String csrfToken) {
        var tempUser = TempUser.builder()
                .email(userInfo.email())
                .thumbnail(userInfo.thumbnail())
                .provider(type.toProvider())
                .csrfToken(csrfToken)
                .build();

        tempUserCacheRepository.save(tempUser, tempUserDuration);
        return tempUser.getToken();
    }

    @Transactional
    public void linkOAuth2User(String userId, String providerEmail, OAuth2Type type) {
        Provider provider = type.toProvider();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        if (user.isNotSameUser(userId)) {
            throw new CustomUserException(UserErrorCode.LINK_AT_LEAST_ONE_USER_PROVIDER);
        }

        UserProvider userProvider = UserProvider.createOAuth2Provider(providerEmail, provider);
        user.addUserProvider(userProvider);
        userRepository.update(user);
    }


    @Transactional
    public void unlinkOAuth2User(String userId, OAuth2Type type) {
        Provider provider = type.toProvider();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        user.deleteProvider(provider);
        userRepository.update(user);
    }

    public UserLoginTokenDto refreshUserToken(String encodedAccessToken, String encodedRefreshToken) {
        refreshTokenHandler.parseEncodedToken(encodedRefreshToken);

        AccessTokenPayload tokenPayload = generateNewEncodedAccessToken(encodedAccessToken);
        String newAccessToken = accessTokenHandler.generateEncodedToken(tokenPayload);
        String newRefreshToken = refreshTokenHandler.generateEncodedToken();

        return UserLoginTokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private void verifyAlreadyExistEmail(String email, Provider provider) {
        boolean alreadyExist = userRepository.existsByEmailAndProvider(email, provider);
        if (alreadyExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
    }

    private void verifyAlreadyExistNickName(String nickName) {
        boolean alreadyExist = userRepository.existsByNickName(nickName);
        if (alreadyExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }

    private UserLoginTokenDto generateUserToken(String userId) {
        String accessToken = accessTokenHandler.generateEncodedToken(new AccessTokenPayload(userId));
        String refreshToken = refreshTokenHandler.generateEncodedToken();

        return UserLoginTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private AccessTokenPayload generateNewEncodedAccessToken(String encodedAccessToken) {
        try {
            var tokenPayload = accessTokenHandler.parseEncodedToken(encodedAccessToken);
            return AccessTokenPayload.builder()
                    .userId(tokenPayload.userId())
                    .build();
        } catch (ExpiredAccessTokenException e) {
            return AccessTokenPayload.builder()
                    .userId(e.getUserId())
                    .build();
        }
    }
}
