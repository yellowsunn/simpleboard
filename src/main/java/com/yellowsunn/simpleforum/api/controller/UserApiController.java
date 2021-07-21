package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.dto.user.UserRegisterDto;
import com.yellowsunn.simpleforum.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        if (bindingResult.hasErrors()) {
            log.error("validation error = {}", bindingResult);
            response.sendError(SC_BAD_REQUEST, "validation error");
            return;
        }

        try {
            userService.register(userDto);
        } catch(DataIntegrityViolationException | IllegalArgumentException e) {
            log.error(e.getMessage());
            response.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }
}
