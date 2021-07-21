package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
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

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Validated @RequestBody UserRegisterDto userDto, BindingResult bindingResult,
                                      HttpServletResponse response) throws IOException {
        try {
            checkValidation(bindingResult);
            userService.register(userDto);
        } catch(IllegalArgumentException e) {
            log.error("error=", e);
            response.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.error("error=", e);
            response.sendError(SC_BAD_REQUEST, "이미 사용중인 아이디입니다.");
        }
    }

    @PostMapping("/login")
    public void login(@Validated @RequestBody UserLoginDto userDto, BindingResult bindingResult,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            checkValidation(bindingResult);
            Long userId = userService.login(userDto);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_ID, userId);

        } catch(IllegalArgumentException e) {
            log.error("error=", e);
            response.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation error");
        }
    }
}
