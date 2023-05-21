package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.service.InternalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class InternalUserController {
    private final InternalUserService internalUserService;

    @GetMapping("/api/internal/v1/users/uuid/{userUUID}")
    public InternalUserInfoDto findUser(@PathVariable String userUUID) {
        return internalUserService.findUserInfo(userUUID);
    }
}
