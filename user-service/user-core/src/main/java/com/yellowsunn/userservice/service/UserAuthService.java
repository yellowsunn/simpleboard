package com.yellowsunn.userservice.service;

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

import java.time.Duration;

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
    public boolean linkOAuth2User(String userUUID, String tempUserToken) {
//        TempUser tempUser = tempUserCacheRepository.findByToken(tempUserToken);
//        if (tempUser == null) {
//            throw new CustomUserException(UserErrorCode.INVALID_TEMP_USER);
//        }
//        User user = userRepository.findByUUID(userUUID)
//                .orElseThrow(() -> new CustomUserException(UserErrorCode.NOT_FOUND_USER));
//        verifyAlreadyExistUserProvider(user.getId(), tempUser.getProvider());
//
//        var userProvider = UserProvider.builder()
//                .user(user)
//                .provider(tempUser.getProvider())
//                .providerEmail(tempUser.getEmail())
//                .build();
//        userProviderRepository.save(userProvider);
//
//        return tempUserCacheRepository.deleteByToken(tempUserToken);
        return true;
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

    private void verifyAlreadyExistUserProvider(Long userId, Provider provider) {
        boolean isExist = userProviderRepository.existsByUserIdAndProvider(userId, provider);
        if (isExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
    }

    private UserLoginTokenDto generateUserToken(User user) {
        String accessToken = accessTokenHandler.generateEncodedToken(new AccessTokenPayload(user.getUuid(), user.getEmail()));
        String refreshToken = refreshTokenHandler.generateEncodedToken();

        return UserLoginTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
