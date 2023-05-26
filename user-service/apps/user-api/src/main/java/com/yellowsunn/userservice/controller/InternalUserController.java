package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.dto.InternalUserSimpleDto;
import com.yellowsunn.userservice.service.InternalUserService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class InternalUserController {
    private final InternalUserService internalUserService;

    @GetMapping("/api/internal/v1/users/{userId}")
    public InternalUserInfoDto findUser(@PathVariable Long userId) {
        return internalUserService.findUserInfo(userId);
    }

    @GetMapping("/api/internal/v1/users")
    public List<InternalUserSimpleDto> findUsers(@RequestParam(name = "id") @Size(max = 200) List<Long> ids) {
        return internalUserService.findUsers(ids);
    }
}
