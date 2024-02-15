package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createEmailUser(UserCreateCommand command) {

        User user = User.createEmailUser(command.userId(), command.email(), command.password(), command.nickname());

        userRepository.save(user);
    }
}
