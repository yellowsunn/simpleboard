package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginCommand;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import com.yellowsunn.userservice.utils.token.AccessTokenGenerator;
import com.yellowsunn.userservice.utils.token.AccessTokenPayload;
import com.yellowsunn.userservice.utils.token.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserEmailAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Transactional
    public boolean signUp(UserEmailSignUpCommand command, String thumbnail) {
        verifyAlreadyExistEmail(command.email());
        verifyAlreadyExistNickName(command.nickName());

        var user = User.builder()
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .nickName(command.nickName())
                .thumbnail(thumbnail)
                .build();

        var savedUser = userRepository.save(user);
        return savedUser != null;
    }

    @Transactional
    public UserLoginDto login(UserLoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .filter(u -> passwordEncoder.matches(command.password(), u.getPassword()))
                .orElseThrow(() -> new CustomUserException(UserErrorCode.INVALID_LOGIN));

        String accessToken = accessTokenGenerator.generateToken(new AccessTokenPayload(user.getUuid(), user.getEmail()));
        String refreshToken = refreshTokenGenerator.generateToken();

        return UserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
}
