package com.yellowsunn.userservice.controller;

import static java.util.Objects.requireNonNull;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.response.ResultResponse;
import com.yellowsunn.userservice.application.UserFacade;
import com.yellowsunn.userservice.application.UserService;
import com.yellowsunn.userservice.application.command.FileUploadCommand;
import com.yellowsunn.userservice.controller.request.UserInfoUpdateRequest;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.exception.CustomIOException;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExternalUserController {

    private final UserFacade userFacade;
    private final UserService userService;

    @GetMapping("/api/v2/users/me")
    public ResultResponse<UserMyInfoDto> findMyInfo(@LoginUser String userId) {
        return ResultResponse.ok(
                userService.findUserInfo(userId)
        );
    }

    @DeleteMapping("/api/v2/users/me")
    public ResultResponse<Boolean> deleteMyInfo(@LoginUser String userId) {
        userService.deleteUserInfo(userId);

        return ResultResponse.ok(true);
    }

    @PutMapping("/api/v2/users/me")
    public ResultResponse<Boolean> updateMyInfo(
            @LoginUser String userId,
            @Valid @RequestBody UserInfoUpdateRequest request
    ) {
        var command = request.toCommand(userId);
        userService.changeUserInfo(command);

        return ResultResponse.ok(true);
    }

    @PatchMapping("/api/v2/users/me/thumbnail")
    public ResultResponse<String> updateMyThumbnail(
            @LoginUser String userId,
            @RequestParam MultipartFile thumbnail
    ) {
        if (isNotImageType(thumbnail.getContentType())) {
            throw new IllegalArgumentException("This file is not an image type.");
        }

        try (var inputStream = thumbnail.getInputStream()) {
            var fileUploadRequest = generateFileUploadRequest(thumbnail, inputStream);
            return ResultResponse.ok(
                    userFacade.updateUserThumbnail(userId, fileUploadRequest)
            );
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

    private boolean isNotImageType(String contentType) {
        return !StringUtils.startsWith(contentType, "image/");
    }

    private FileUploadCommand generateFileUploadRequest(MultipartFile thumbnail, InputStream inputStream) {
        return FileUploadCommand.builder()
                .inputStream(inputStream)
                .originalFileName(requireNonNull(thumbnail.getOriginalFilename()))
                .build();
    }
}
