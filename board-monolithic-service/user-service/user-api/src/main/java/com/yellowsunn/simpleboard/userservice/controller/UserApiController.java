package com.yellowsunn.simpleboard.userservice.controller;

import com.yellowsunn.common.annotation.LoginId;
import com.yellowsunn.common.constant.SessionConst;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import com.yellowsunn.simpleboard.userservice.dto.UserGetDto;
import com.yellowsunn.simpleboard.userservice.dto.UserLoginDto;
import com.yellowsunn.simpleboard.userservice.dto.UserPatchRequestDto;
import com.yellowsunn.simpleboard.userservice.dto.UserRegisterDto;
import com.yellowsunn.simpleboard.userservice.repository.UserRepository;
import com.yellowsunn.simpleboard.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;
//    private final RefererFilter refererFilter;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Validated @RequestBody UserRegisterDto userDto, BindingResult bindingResult) {
//        refererFilter.check("/register");
        checkValidation(bindingResult);
        userService.register(userDto);
    }

    @GetMapping
    public Page<UserGetDto> users(@LoginId Long userId,
                                  @RequestParam(required = false) String search,
                                  @RequestParam(required = false) Long cursor,
                                  Pageable pageable) {
        return userService.findUsers(userId, search, cursor, pageable);
    }

    @PostMapping("/login")
    public void login(@Validated @RequestBody UserLoginDto userDto, BindingResult bindingResult,
                      HttpServletRequest request) {
        checkValidation(bindingResult);
        doLogin(request, userDto);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        invalidateLoginSession(request);
    }

    @GetMapping("/current")
    public UserGetDto findCurrentLoggedInUser(@LoginId Long userId) {
        return userService.findUserById(userId);
    }

    @PatchMapping("/current")
    public void changePassword(@LoginId Long userId,
                               @Validated @RequestBody UserPatchRequestDto userPatchRequestDto,
                               BindingResult bindingResult) {
//        refererFilter.check("/users/myinfo/change");
        checkValidation(bindingResult);
        userService.changePassword(userId, userPatchRequestDto);
    }

    @DeleteMapping("/current")
    public void deleteCurrentUser(@LoginId Long userId, HttpServletRequest request) {
//        refererFilter.check("/users/myinfo", "/users");
        userService.deleteCurrentUser(userId);
        invalidateLoginSession(request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
//        refererFilter.check("/users");
        userService.deleteById(userId);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

    private void doLogin(HttpServletRequest request, UserLoginDto userDto) {
        User user = userService.login(userDto);
        makeLoginSessionAttribute(request, user);
    }

    private void makeLoginSessionAttribute(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USER_ID, user.getId());
        session.setAttribute(SessionConst.USER_ROLE, user.getRole());
    }

    private void invalidateLoginSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
