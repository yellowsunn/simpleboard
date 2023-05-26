package com.yellowsunn.userservice.facade;

import com.yellowsunn.userservice.file.FileUploadRequest;
import com.yellowsunn.userservice.http.image.ImageHttpClient;
import com.yellowsunn.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;
    private final ImageHttpClient imageHttpClient;

    public String updateUserThumbnail(String uuid, FileUploadRequest request) {
        String updatedThumbnail = imageHttpClient.uploadThumbnailImage(request.originalFileName(), request.inputStream());
        userService.changeUserThumbnail(uuid, updatedThumbnail);
        return updatedThumbnail;
    }
}
