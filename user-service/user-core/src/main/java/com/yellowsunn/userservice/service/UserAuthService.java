package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.dto.UserEmailLoginCommand;
import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.dto.UserOAuth2SignUpCommand;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import com.yellowsunn.userservice.utils.token.AccessTokenGenerator;
import com.yellowsunn.userservice.utils.token.AccessTokenPayload;
import com.yellowsunn.userservice.utils.token.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final TempUserCacheRepository tempUserCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

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
    public boolean signUpOAuth2(UserOAuth2SignUpCommand command, String thumbnail) {
        TempUser tempUser = tempUserCacheRepository.findByToken(command.tempUserToken());
        if (tempUser == null) {
            throw new CustomUserException(UserErrorCode.INVALID_TEMP_USER);
        }
        verifyAlreadyExistEmail(tempUser.getEmail());
        verifyAlreadyExistNickName(command.nickName());

        var user = User.oauth2UserBuilder()
                .email(tempUser.getEmail())
                .nickName(command.nickName())
                .thumbnail(StringUtils.defaultIfBlank(tempUser.getThumbnail(), thumbnail))
                .build();
        var savedUser = userRepository.save(user);

        var userProvider = UserProvider.builder()
                .user(savedUser)
                .provider(tempUser.getProvider())
                .providerEmail(tempUser.getEmail())
                .build();
        userProviderRepository.save(userProvider);

        return tempUserCacheRepository.deleteByToken(command.tempUserToken());
    }

    @Transactional
    public UserLoginDto loginEmail(UserEmailLoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .filter(u -> userProviderRepository.existsByUserIdAndProvider(u.getId(), Provider.EMAIL))
                .filter(u -> passwordEncoder.matches(command.password(), u.getPassword()))
                .orElseThrow(() -> new CustomUserException(UserErrorCode.INVALID_LOGIN));

        String accessToken = accessTokenGenerator.generateEncodedToken(new AccessTokenPayload(user.getUuid(), user.getEmail()));
        String refreshToken = refreshTokenGenerator.generateEncodedToken();

        return UserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public boolean linkOAuth2User(String userUUID, String tempUserToken) {
        TempUser tempUser = tempUserCacheRepository.findByToken(tempUserToken);
        if (tempUser == null) {
            throw new CustomUserException(UserErrorCode.INVALID_TEMP_USER);
        }
        User user = userRepository.findByUUID(userUUID)
                .orElseThrow(() -> new CustomUserException(UserErrorCode.NOT_FOUND_USER));
        verifyAlreadyExistUserProvider(user.getId(), tempUser.getProvider());

        var userProvider = UserProvider.builder()
                .user(user)
                .provider(tempUser.getProvider())
                .providerEmail(tempUser.getEmail())
                .build();
        userProviderRepository.save(userProvider);

        return tempUserCacheRepository.deleteByToken(tempUserToken);
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
}
