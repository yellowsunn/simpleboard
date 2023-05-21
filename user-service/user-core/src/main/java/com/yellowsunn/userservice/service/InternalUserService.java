package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InternalUserService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;

    @Transactional(readOnly = true)
    public InternalUserInfoDto findUserInfo(String userUUID) {
        return userRepository.findByUUID(userUUID)
                .map(user -> {
                    var providers = userProviderRepository.findProvidersByUserId(user.getId());
                    return InternalUserInfoDto.from(user, providers);
                }).orElse(null);
    }
}
