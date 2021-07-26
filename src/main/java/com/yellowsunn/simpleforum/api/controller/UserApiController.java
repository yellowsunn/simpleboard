package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import com.yellowsunn.simpleforum.exception.NotFoundUserException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @ExceptionHandler(value = {IllegalArgumentException.class, DataIntegrityViolationException.class})
    public void badRequest(HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof DataIntegrityViolationException) {
            response.sendError(SC_BAD_REQUEST, "이미 사용중인 아이디입니다.");
        } else {
            response.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }

    @ExceptionHandler(NotFoundUserException.class)
    public void notFoundUserException(HttpServletRequest request, HttpServletResponse response,
                                      NotFoundUserException e) throws IOException {
        invalidateLoginSession(request);
        response.sendError(SC_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public void passwordMismatch(HttpServletResponse response, PasswordMismatchException e) throws IOException {
        response.sendError(SC_FORBIDDEN, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Validated @RequestBody UserRegisterDto userDto, BindingResult bindingResult) {
        checkValidation(bindingResult);
        userService.register(userDto);
    }

    @PostMapping("/login")
    public void login(@Validated @RequestBody UserLoginDto userDto, BindingResult bindingResult,
                      HttpServletRequest request) {
        checkValidation(bindingResult);
        doLogin(request, userDto);
    }

    @GetMapping("/current")
    public UserGetDto findCurrentLoggedInUser(@SessionAttribute(SessionConst.USER_ID) Long userId) {
        return userService.findUserById(userId);
    }

    @PatchMapping("/current")
    public void changePassword(@SessionAttribute(SessionConst.USER_ID) Long userId,
                               @Validated @RequestBody UserPatchRequestDto userPatchRequestDto,
                               BindingResult bindingResult) {
        checkValidation(bindingResult);
        userService.changePassword(userId, userPatchRequestDto);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation error");
        }
    }

    private void doLogin(HttpServletRequest request, UserLoginDto userDto) {
        Long userId = userService.login(userDto);
        makeLoginSessionAttribute(request, userId);
    }

    private void makeLoginSessionAttribute(HttpServletRequest request, Long userId) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USER_ID, userId);
    }

    private void invalidateLoginSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
