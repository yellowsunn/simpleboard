package com.yellowsunn.userservice.service;

import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.dto.UserInfoUpdateCommand;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;

    private static final String USER_UUID_ID_NULL_MESSAGE = "User userUUID must not be null.";

    @Transactional(readOnly = true)
    public UserMyInfoDto findUserInfo(String uuid) {
        Assert.notNull(uuid, USER_UUID_ID_NULL_MESSAGE);

        User user = getUserByUUID(uuid);
        List<Provider> providers = userProviderRepository.findProvidersByUserId(user.getId());

        return UserMyInfoDto.fromUser(user, providers);
    }

    @Transactional
    public boolean deleteUserInfo(String uuid) {
        Assert.notNull(uuid, USER_UUID_ID_NULL_MESSAGE);

        return userRepository.findByUUID(uuid)
                .filter(user -> userProviderRepository.deleteByUserId(user.getId()))
                .map(userRepository::delete)
                .orElse(true);
    }

    @Transactional
    public boolean changeUserThumbnail(String uuid, String thumbnail) {
        Assert.notNull(uuid, USER_UUID_ID_NULL_MESSAGE);

        User user = getUserByUUID(uuid);
        return user.changeThumbnail(thumbnail);
    }

    @Transactional
    public boolean changeUserInfo(UserInfoUpdateCommand command) {
        Assert.notNull(command.userUUID(), USER_UUID_ID_NULL_MESSAGE);

        User user = getUserByUUID(command.userUUID());
        checkValidNickName(user.getId(), command.nickName());

        return user.changeNickName(command.nickName());
    }

    private User getUserByUUID(String uuid) {
        return userRepository.findByUUID(uuid)
                .orElseThrow(UserNotFoundException::new);
    }

    private void checkValidNickName(Long userId, String nickName) {
        boolean isExist = userRepository.findByNickName(nickName)
                .map(user -> ObjectUtils.notEqual(user.getId(), userId))
                .orElse(false);

        if (isExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }
}
