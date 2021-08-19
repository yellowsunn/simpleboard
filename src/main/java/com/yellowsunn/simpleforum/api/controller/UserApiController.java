package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.argumentresolver.LoginId;
import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import com.yellowsunn.simpleforum.api.util.RefererFilter;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final RefererFilter refererFilter;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Validated @RequestBody UserRegisterDto userDto, BindingResult bindingResult) {
        refererFilter.check("/register");
        checkValidation(bindingResult);
        userService.register(userDto);
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
        refererFilter.check("/users/myinfo/change");
        checkValidation(bindingResult);
        userService.changePassword(userId, userPatchRequestDto);
    }

    @DeleteMapping("/current")
    public void deleteCurrentUser(@LoginId Long userId, HttpServletRequest request) {
        userService.deleteCurrentUser(userId);
        invalidateLoginSession(request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation error");
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
