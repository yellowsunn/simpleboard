package com.yellowsunn.userservice.service;

import com.yellowsunn.common.exception.LoginUserNotFoundException;
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

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;

    @Transactional(readOnly = true)
    public UserMyInfoDto findUserInfo(Long userId) {
        User user = getUserById(userId);

        List<Provider> providers = userProviderRepository.findProvidersByUserId(user.getId());

        return UserMyInfoDto.fromUser(user, providers);
    }

    @Transactional
    public boolean deleteUserInfo(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> userProviderRepository.deleteByUserId(user.getId()))
                .map(userRepository::delete)
                .orElse(true);
    }

    @Transactional
    public boolean changeUserThumbnail(Long userId, String thumbnail) {
        User user = getUserById(userId);
        return user.changeThumbnail(thumbnail);
    }

    @Transactional
    public boolean changeUserInfo(UserInfoUpdateCommand command) {
        User user = getUserById(command.userId());
        checkValidNickName(user.getId(), command.nickName());

        return user.changeNickName(command.nickName());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(LoginUserNotFoundException::new);
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
