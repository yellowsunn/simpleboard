package com.yellowsunn.userservice.presentation.controller;

import com.yellowsunn.userservice.application.UserService;
import com.yellowsunn.userservice.utils.UuidHolder;
import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.presentation.request.EmailSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UuidHolder uuidHolder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v3/auth/email/signup")
    public String signUpEmail(@RequestBody EmailSignUpRequest request) {
        String userId = uuidHolder.random();

        UserCreateCommand command = UserCreateCommand.builder()
                .userId(userId)
                .email(request.email())
                .password(request.password())
                .nickname(request.nickname())
                .build();

        userService.createEmailUser(command);

        return userId;
    }
}
