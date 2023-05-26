package com.yellowsunn.userservice.controller;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.userservice.dto.UserInfoUpdateRequestDto;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.exception.CustomIOException;
import com.yellowsunn.userservice.facade.UserFacade;
import com.yellowsunn.userservice.file.FileUploadRequest;
import com.yellowsunn.userservice.service.UserService;
import jakarta.validation.Valid;
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

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ExternalUserController {
    private final UserFacade userFacade;
    private final UserService userService;

    @GetMapping("/api/v2/users/my-info")
    public UserMyInfoDto findMyInfo(@LoginUser String uuid) {
        return userService.findUserInfo(uuid);
    }

    @DeleteMapping("/api/v2/users/my-info")
    public boolean deleteMyInfo(@LoginUser String uuid) {
        return userService.deleteUserInfo(uuid);
    }

    @PutMapping("/api/v2/users/my-info")
    public boolean updateMyInfo(@LoginUser String uuid,
                                @Valid @RequestBody UserInfoUpdateRequestDto requestDto) {
        var command = requestDto.toCommand(uuid);
        return userService.changeUserInfo(command);
    }

    @PatchMapping("/api/v2/users/my-info/thumbnail")
    public String updateMyThumbnail(@LoginUser String uuid,
                                    @RequestParam MultipartFile thumbnail) {
        if (isNotImageType(thumbnail.getContentType())) {
            throw new IllegalArgumentException("This file is not an image type.");
        }

        try (var inputStream = thumbnail.getInputStream()) {
            var fileUploadRequest = generateFileUploadRequest(thumbnail, inputStream);
            return userFacade.updateUserThumbnail(uuid, fileUploadRequest);
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

    private boolean isNotImageType(String contentType) {
        return !StringUtils.startsWith(contentType, "image/");
    }

    private FileUploadRequest generateFileUploadRequest(MultipartFile thumbnail, InputStream inputStream) {
        return FileUploadRequest.builder()
                .inputStream(inputStream)
                .originalFileName(requireNonNull(thumbnail.getOriginalFilename()))
                .build();
    }
}
