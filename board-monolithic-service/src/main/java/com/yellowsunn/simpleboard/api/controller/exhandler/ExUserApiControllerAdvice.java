package com.yellowsunn.simpleboard.api.controller.exhandler;


import com.yellowsunn.simpleboard.userservice.controller.UserApiController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@RestControllerAdvice(assignableTypes = UserApiController.class)
public class ExUserApiControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void badRequest(HttpServletResponse response, DataIntegrityViolationException e) throws IOException {
        response.sendError(SC_BAD_REQUEST, "이미 사용중인 아이디입니다.");
    }
}
