package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.application.command.UserInfoUpdateCommand;
import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserMyInfoDto findUserInfo(String userId) {
        User user = userRepository.getByUserId(userId);

        return UserMyInfoDto.from(user);
    }

    @Transactional
    public void deleteUserInfo(String userId) {
        User user = userRepository.getByUserId(userId);

        userRepository.delete(user);
    }

    @Transactional
    public void changeUserThumbnail(String userId, String thumbnail) {
        User user = userRepository.getByUserId(userId);
        user.changeThumbnail(thumbnail);

        userRepository.update(user);
    }

    @Transactional
    public void changeUserInfo(UserInfoUpdateCommand command) {
        checkValidNickName(command.userId(), command.nickName());

        User user = userRepository.getByUserId(command.userId());
        user.changeNickName(command.nickName());

        userRepository.update(user);
    }

    private void checkValidNickName(String userId, String nickName) {
        boolean isExist = userRepository.findByNickName(nickName)
                .map(user -> ObjectUtils.notEqual(user.getUserId(), userId))
                .orElse(false);

        if (isExist) {
            throw new CustomUserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }
}
