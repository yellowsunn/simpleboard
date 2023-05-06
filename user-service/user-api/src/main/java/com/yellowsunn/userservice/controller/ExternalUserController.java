package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.dto.LoginRequestDto;
import com.yellowsunn.userservice.dto.SignUpRequestDto;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalUserController {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/users")
    public boolean signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        return true;
    }

    @PostMapping("/api/v2/users/login")
    public UserLoginDto login(@RequestBody LoginRequestDto requestDto) {
        return new UserLoginDto();
    }

    @GetMapping("/api/v2/users/my-info")
    public UserMyInfoDto findMyInfo(@RequestHeader("x-user-id") Long userId) {
        return new UserMyInfoDto();
    }

    @DeleteMapping("/api/v2/users/my-info")
    public boolean deleteMyInfo(@RequestHeader("x-user-id") Long userId) {
        return true;
    }
}
