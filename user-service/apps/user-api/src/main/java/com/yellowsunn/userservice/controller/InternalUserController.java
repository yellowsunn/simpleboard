package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.application.InternalUserService;
import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.dto.InternalUserSimpleDto;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class InternalUserController {

    private final InternalUserService internalUserService;

    @GetMapping("/api/internal/v1/users/{userId}")
    public InternalUserInfoDto findUser(@PathVariable String userId) {
        return internalUserService.findUserInfo(userId);
    }

    @GetMapping("/api/internal/v1/users")
    public List<InternalUserSimpleDto> findUsers(@RequestParam(name = "id") @Size(max = 200) List<String> userIds) {
        return internalUserService.findUsers(userIds);
    }
}
