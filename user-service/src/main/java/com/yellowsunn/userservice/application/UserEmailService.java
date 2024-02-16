package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.domain.dto.EmailUserInfoDto;
import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.UserRepository;
import com.yellowsunn.userservice.domain.vo.UserId;
import com.yellowsunn.userservice.exception.EmailLoginFailedException;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class UserEmailService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void createEmailUser(UserCreateCommand command) {

        User user = User.createEmailUser(command.userId(), command.email(), command.nickname(),
                passwordEncoder.encode(command.password()));

        userRepository.save(user);
    }

    public UserId login(String email, String password) {

        EmailUserInfoDto emailUserInfoDto = userRepository.findEmailUserInfo(email)
                .filter(emailUserInfo -> passwordEncoder.match(password, emailUserInfo.password()))
                .orElseThrow(EmailLoginFailedException::new);

        return UserId.fromString(emailUserInfoDto.userId());
    }
}
