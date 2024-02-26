package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.dto.InternalUserSimpleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InternalUserService {

    private final UserRepository userRepository;

    public InternalUserInfoDto findUserInfo(String userId) {
        return userRepository.findByUserId(userId)
                .map(InternalUserInfoDto::from)
                .orElse(null);
    }

    public List<InternalUserSimpleDto> findUsers(List<String> userIds) {
        return userRepository.findByUserIds(userIds).stream()
                .map(InternalUserSimpleDto::from)
                .toList();
    }
}
