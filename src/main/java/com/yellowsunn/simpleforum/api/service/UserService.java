package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.repository.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
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
    public User login(UserLoginDto userLoginDto) {
        Optional<User> userOptional = userRepository.findByUsername(userLoginDto.getUsername());
        if (isUserNotFoundOrPasswordNotMatch(userLoginDto.getPassword(), userOptional)) {
            throw new NotFoundException("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
        }

        return userOptional.get();
    }

    @Transactional(readOnly = true)
    public UserGetDto findUserById(Long id) {
        User user = findUser(id);
        return new UserGetDto(user);
    }

    @Transactional
    public void changePassword(Long id, UserPatchRequestDto dto) {
        User user = findUser(id);
        checkPasswordMatched(dto.getPassword(), user);
        user.changePassword(encodedPassword(dto.getNewPassword()));
    }

    @Transactional
    public void deleteCurrentUser(Long id) {
        User user = findUser(id);
        user.deleteUsername();
    }

    @Transactional
    public void deleteById(Long id) {
        User user = findUser(id);
        checkRoleIsNotAdmin(user);
        user.deleteUsername();
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
    }

    private User userRegisterDtoToUser(UserRegisterDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(encodedPassword(userDto.getPassword()))
                .build();
    }

    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isUserNotFoundOrPasswordNotMatch(String password, Optional<User> userOptional) {
        return userOptional.isEmpty() || hasPasswordNotMatched(password, userOptional.get());
    }

    private boolean hasPasswordNotMatched(String password, User user) {
        return !passwordEncoder.matches(password, user.getPassword());
    }

    private void checkPasswordMatched(String password, User user) {
        if (hasPasswordNotMatched(password, user)) {
            throw new PasswordMismatchException();
        }
    }

    private void checkRoleIsNotAdmin(User user) {
        if (user.getRole() == Role.ADMIN) {
            throw new ForbiddenException("관리자는 탈퇴 처리 시킬 수 없습니다.");
        }
    }
}
