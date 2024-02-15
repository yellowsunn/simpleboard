package com.yellowsunn.userservice.application.port.out;

import com.yellowsunn.userservice.domain.dto.EmailUserInfoDto;
import com.yellowsunn.userservice.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<EmailUserInfoDto> findEmailUserInfo(String email);
}
