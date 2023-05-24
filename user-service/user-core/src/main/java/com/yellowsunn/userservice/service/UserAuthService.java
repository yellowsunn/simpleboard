package com.yellowsunn.userservice.service;

import com.yellowsunn.common.exception.ExpiredAccessTokenException;
import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.AccessTokenPayload;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.dto.UserEmailLoginCommand;
import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.dto.UserOAuth2SignUpCommand;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.http.OAuth2UserInfo;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final TempUserCacheRepository tempUserCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenHandler accessTokenHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    private final Duration tempUserDuration = Duration.ofMinutes(30);

    @Transactional
    public boolean signUpEmail(UserEmailSignUpCommand command, String thumbnail) {
        verifyAlreadyExistEmail(command.email());
        verifyAlreadyExistNickName(command.nickName());

        var emailUser = User.emailUserBuilder()
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .nickName(command.nickName())
                .thumbnail(thumbnail)
                .build();
        var savedUser = userRepository.save(emailUser);

        var userProvider = UserProvider.builder()
                .user(savedUser)
                .provider(Provider.EMAIL)
                .providerEmail(savedUser.getEmail())
                .build();
        var savedUserProvider = userProviderRepository.save(userProvider);
        return savedUserProvider != null;
    }

    @Transactional
    public UserLoginTokenDto signUpOAuth2(UserOAuth2SignUpCommand command, TempUser tempUser, String thumbnail) {
        verifyAlreadyExistEmail(tempUser.getEmail());
        verifyAlreadyExistNickName(command.nickName());

        var user = User.oauth2UserBuilder()
                .email(tempUser.getEmail())
                .nickName(command.nickName())
                .thumbnail(thumbnail)
                .build();
        var savedUser = userRepository.save(user);

        var userProvider = UserProvider.builder()
                .user(savedUser)
                .provider(tempUser.getProvider())
                .providerEmail(tempUser.getEmail())
                .build();
        userProviderRepository.save(userProvider);

        return generateUserToken(savedUser);
    }

    @Transactional(readOnly = true)
    public UserLoginTokenDto loginEmail(UserEmailLoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .filter(u -> userProviderRepository.existsByUserIdAndProvider(u.getId(), Provider.EMAIL))
                .filter(u -> passwordEncoder.matches(command.password(), u.getPassword()))
                .orElseThrow(() -> new CustomUserException(UserErrorCode.INVALID_LOGIN));

        return generateUserToken(user);
    }

    @Transactional(readOnly = true)
    public UserLoginTokenDto loginOAuth2(OAuth2UserInfo userInfo, OAuth2Type type) {
        User user = userProviderRepository.findByProviderEmailAndProvider(userInfo.email(), type.toProvider())
                .map(UserProvider::getUser)
                .orElse(null);
        if (user == null) {
            return null;
        }
        return generateUserToken(user);
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
    public boolean linkOAuth2User(String userUUID, String providerEmail, OAuth2Type type) {
        Provider provider = type.toProvider();
        Assert.notNull(provider, "OAuth2 provider must not be null.");

        User user = userRepository.findByUUID(userUUID)
                .orElseThrow(UserNotFoundException::new);

        boolean isAlreadyFinished = checkAlreadyExistProvider(user.getId(), providerEmail, provider);
        if (isAlreadyFinished) {
            return true;
        }

        var userProvider = UserProvider.builder()
                .user(user)
                .provider(provider)
                .providerEmail(providerEmail)
                .build();
        return userProviderRepository.save(userProvider) != null;
    }


    @Transactional
    public boolean unlinkOAuth2User(String userUUID, OAuth2Type type) {
        Provider provider = type.toProvider();
        Assert.notNull(provider, "OAuth2 provider must not be null.");

        User user = userRepository.findByUUID(userUUID)
                .orElseThrow(UserNotFoundException::new);

        // TODO: 동시성 이슈 고려 필요
        checkAtLeastOneProvider(user.getId());

        return userProviderRepository.deleteByUserIdAndProvider(user.getId(), provider);
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

    private void verifyAlreadyExistEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new CustomUserException(UserErrorCode.ALREADY_EXIST_EMAIL);
                });
    }

    private void verifyAlreadyExistNickName(String nickName) {
        boolean isExist = userRepository.existsByNickName(nickName);
        if (isExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }

    private boolean checkAlreadyExistProvider(Long userId, String providerEmail, Provider provider) {
        // 사용하지 않는 새로운 provider 인 경우
        var userProviderOptional = userProviderRepository.findByProviderEmailAndProvider(providerEmail, provider);
        if (userProviderOptional.isEmpty()) {
            return false;
        }

        // 내 계정에서 이미 사용중인 Provider 경우
        var userProvider = userProviderOptional.get();
        if (isMyUserProvider(userId, userProvider)) {
            return true;
        }

        // 다른 계정에서 사용중인 Provider라면 예외 반환
        throw new CustomUserException(UserErrorCode.LINK_AT_OTHER_USER_PROVIDER);
    }

    private boolean isMyUserProvider(Long userId, UserProvider userProvider) {
        return Objects.equals(userId, userProvider.getUser().getId());
    }

    private UserLoginTokenDto generateUserToken(User user) {
        String accessToken = accessTokenHandler.generateEncodedToken(new AccessTokenPayload(user.getUuid(), user.getEmail()));
        String refreshToken = refreshTokenHandler.generateEncodedToken();

        return UserLoginTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void checkAtLeastOneProvider(Long userId) {
        long providerCount = userProviderRepository.countProvidersByUserId(userId);
        if (providerCount <= 1) {
            throw new CustomUserException(UserErrorCode.LINK_AT_LEAST_ONE_USER_PROVIDER);
        }
    }

    private AccessTokenPayload generateNewEncodedAccessToken(String encodedAccessToken) {
        String email;
        String userUUID;
        try {
            var tokenPayload = accessTokenHandler.parseEncodedToken(encodedAccessToken);
            email = tokenPayload.email();
            userUUID = tokenPayload.uuid();
        } catch (ExpiredAccessTokenException e) {
            email = e.getEmail();
            userUUID = e.getUserUUID();
        }

        return AccessTokenPayload.builder()
                .email(email)
                .uuid(userUUID)
                .build();
    }
}
