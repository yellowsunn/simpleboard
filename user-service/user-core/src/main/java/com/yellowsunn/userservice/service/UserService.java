package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserMyInfoDto findUserInfo(Long userId) {
        Assert.notNull(userId, "userId must not be null.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomUserException(UserErrorCode.NOT_FOUND_USER));

        return UserMyInfoDto.fromUser(user);
    }

    @Transactional
    public boolean deleteUserInfo(Long userId) {
        Assert.notNull(userId, "userId must not be null.");

        return userRepository.findById(userId)
                .map(userRepository::delete)
                .orElse(true);
    }
}
