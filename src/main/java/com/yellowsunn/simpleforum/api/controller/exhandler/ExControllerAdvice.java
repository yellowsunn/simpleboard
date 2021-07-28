package com.yellowsunn.simpleforum.api.controller.exhandler;

import com.yellowsunn.simpleforum.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public void unauthorized(UnauthorizedException e, HttpServletResponse response) throws IOException {
        response.sendError(SC_UNAUTHORIZED, e.getMessage());
    }
}
