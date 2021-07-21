package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.security.encoder.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRegisterDto userDto) {
        User user = userRegisterDtoToUser(userDto);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Long login(UserLoginDto userLoginDto) {
        Optional<User> userOptional = userRepository.findByUsername(userLoginDto.getUsername());
        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(userLoginDto.getPassword(), userOptional.get().getPassword())) {
            throw new IllegalArgumentException("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
        }

        return userOptional.get().getId();
    }

    private User userRegisterDtoToUser(UserRegisterDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .nickname(userDto.getNickname())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
    }
}
