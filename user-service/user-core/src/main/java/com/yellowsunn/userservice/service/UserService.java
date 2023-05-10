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

    private static final String USER_ID_NULL_MESSAGE = "userId must not be null.";

    @Transactional(readOnly = true)
    public UserMyInfoDto findUserInfo(Long userId) {
        Assert.notNull(userId, USER_ID_NULL_MESSAGE);

        User user = getUserById(userId);
        return UserMyInfoDto.fromUser(user);
    }

    @Transactional
    public boolean deleteUserInfo(Long userId) {
        Assert.notNull(userId, USER_ID_NULL_MESSAGE);

        return userRepository.findById(userId)
                .map(userRepository::delete)
                .orElse(true);
    }

    @Transactional
    public boolean changeUserThumbnail(Long userId, String thumbnail) {
        Assert.notNull(userId, USER_ID_NULL_MESSAGE);

        User user = getUserById(userId);
        return user.changeThumbnail(thumbnail);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomUserException(UserErrorCode.NOT_FOUND_USER));
    }
}
