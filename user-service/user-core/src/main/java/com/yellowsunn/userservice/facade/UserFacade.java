package com.yellowsunn.userservice.facade;

import com.yellowsunn.userservice.file.FileStorage;
import com.yellowsunn.userservice.file.FileUploadRequest;
import com.yellowsunn.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;
    private final FileStorage fileStorage;

    private static final String THUMBNAIL_PATH = "thumbnail";

    public String updateUserThumbnail(Long userId, FileUploadRequest request) {
        String updatedThumbnail = fileStorage.uploadFile(request, THUMBNAIL_PATH);
        userService.changeUserThumbnail(userId, updatedThumbnail);
        return updatedThumbnail;
    }
}
