package com.yellowsunn.userservice.presentation.controller;

import com.yellowsunn.userservice.application.UserService;
import com.yellowsunn.userservice.application.UuidHolder;
import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.presentation.request.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UuidHolder uuidHolder;

    @PostMapping("/api/v3/users/signup/email")
    public String signUpEmail(@RequestBody UserCreateRequest request) {
        String userId = uuidHolder.random();

        UserCreateCommand command = UserCreateCommand.builder()
                .userId(userId)
                .email(request.email())
                .password(request.password())
                .nickname(request.nickname())
                .build();

        userService.createUser(command);

        return userId;
    }
}
