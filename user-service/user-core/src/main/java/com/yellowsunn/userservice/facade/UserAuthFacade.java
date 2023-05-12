package com.yellowsunn.userservice.facade;

import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserOAuth2SignUpCommand;
import com.yellowsunn.userservice.file.FileStorage;
import com.yellowsunn.userservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthFacade {
    private final UserAuthService userAuthService;
    private final FileStorage fileStorage;

    public boolean signUpEmail(UserEmailSignUpCommand command) {
        String thumbnail = fileStorage.getDefaultThumbnail();
        return userAuthService.signUpEmail(command, thumbnail);
    }

    public boolean signUpOAuth2(UserOAuth2SignUpCommand command) {
        String thumbnail = fileStorage.getDefaultThumbnail();
        return userAuthService.signUpOAuth2(command, thumbnail);
    }
}
