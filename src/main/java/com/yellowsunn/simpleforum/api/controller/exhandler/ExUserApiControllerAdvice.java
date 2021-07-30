package com.yellowsunn.simpleforum.api.controller.exhandler;


import com.yellowsunn.simpleforum.api.controller.UserApiController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@RestControllerAdvice(assignableTypes = UserApiController.class)
public class ExUserApiControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void badRequest(HttpServletResponse response, DataIntegrityViolationException e) throws IOException {
        response.sendError(SC_BAD_REQUEST, "이미 사용중인 아이디입니다.");
    }
}
