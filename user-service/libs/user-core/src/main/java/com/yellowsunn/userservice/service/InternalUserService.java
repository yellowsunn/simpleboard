package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.dto.InternalUserSimpleDto;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@RequiredArgsConstructor
@Service
public class InternalUserService {
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;

    @Transactional(readOnly = true)
    public InternalUserInfoDto findUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    var providers = userProviderRepository.findProvidersByUserId(user.getId());
                    return InternalUserInfoDto.from(user, providers);
                }).orElse(null);
    }

    public List<InternalUserSimpleDto> findUsers(List<Long> userIds) {
        return userRepository.findByIds(emptyIfNull(userIds)).stream()
                .map(InternalUserSimpleDto::from)
                .toList();
    }
}
