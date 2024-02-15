package com.yellowsunn.userservice.presentation.controller;

import com.yellowsunn.userservice.application.port.out.UuidHolder;
import com.yellowsunn.userservice.application.service.UserEmailService;
import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.vo.UserId;
import com.yellowsunn.userservice.presentation.request.EmailLoginRequest;
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

    private final UserEmailService userEmailService;
    private final UuidHolder uuidHolder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v3/auth/email/signup")
    public String emailSignUp(@RequestBody EmailSignUpRequest request) {
        UserId userId = UserId.fromString(uuidHolder.random());
        UserCreateCommand command = request.toUserCreateCommand(userId);

        userEmailService.createEmailUser(command);

        return userId.toString();
    }

    @PostMapping("/api/v3/auth/email/login")
    public String emailLogin(@RequestBody EmailLoginRequest request) {
        UserId userId = userEmailService.login(request.email(), request.password());

        return userId.toString();
    }
}
