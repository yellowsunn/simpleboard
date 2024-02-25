package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.application.command.FileUploadCommand;
import com.yellowsunn.userservice.infrastructure.http.image.ImageHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;
    private final ImageHttpClient imageHttpClient;

    public String updateUserThumbnail(Long userId, FileUploadCommand request) {
        String updatedThumbnail = imageHttpClient.uploadThumbnailImage(request.originalFileName(),
                request.inputStream());
        userService.changeUserThumbnail(userId, updatedThumbnail);
        return updatedThumbnail;
    }
}
