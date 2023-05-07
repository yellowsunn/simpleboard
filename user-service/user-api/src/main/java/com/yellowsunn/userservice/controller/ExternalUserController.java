package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.dto.EmailSignUpRequestDto;
import com.yellowsunn.userservice.dto.LoginRequestDto;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.service.UserEmailAuthService;
import com.yellowsunn.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.yellowsunn.userservice.constant.RequestConst.USER_ID;

@RequiredArgsConstructor
@RestController
public class ExternalUserController {
    private final UserService userService;
    private final UserEmailAuthService userEmailAuthService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/users")
    public boolean emailSignUp(@Valid @RequestBody EmailSignUpRequestDto requestDto) {
        var command = requestDto.toUserSignUpCommand();
        return userEmailAuthService.signUp(command);
    }

    @PostMapping("/api/v2/users/login")
    public UserLoginDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        var command = requestDto.toUserLoginCommand();
        return userEmailAuthService.login(command);
    }

    @GetMapping("/api/v2/users/my-info")
    public UserMyInfoDto findMyInfo(@RequestHeader(USER_ID) Long userId) {
        return userService.findUserInfo(userId);
    }

    @DeleteMapping("/api/v2/users/my-info")
    public boolean deleteMyInfo(@RequestHeader(USER_ID) Long userId) {
        return userService.deleteUserInfo(userId);
    }

    @PatchMapping("/api/v2/users/my-info/thumbnail")
    public String updateMyThumbnail(@RequestParam MultipartFile thumbnail) {
        return thumbnail.getName();
    }
}
