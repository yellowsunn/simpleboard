package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void createEmailUser(UserCreateCommand command) {

        User user = User.createEmailUser(command.userId(), command.email(), command.nickname(),
                passwordEncoder.encode(command.password()));

        userRepository.save(user);
    }
}