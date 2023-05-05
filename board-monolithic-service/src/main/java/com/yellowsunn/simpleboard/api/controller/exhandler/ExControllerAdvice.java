package com.yellowsunn.simpleboard.api.controller.exhandler;

import com.yellowsunn.common.exception.ForbiddenException;
import com.yellowsunn.simpleboard.exception.InvalidReferException;
import com.yellowsunn.common.exception.NotFoundException;
import com.yellowsunn.common.exception.PasswordMismatchException;
import com.yellowsunn.simpleboard.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public void unauthorized(UnauthorizedException e, HttpServletResponse response) throws IOException {
        response.sendError(SC_UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public void notFoundException(HttpServletRequest request, HttpServletResponse response,
                                  NotFoundException e) throws IOException {
        response.sendError(SC_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = {PasswordMismatchException.class, ForbiddenException.class})
    public void forbidden(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(SC_FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, InvalidReferException.class})
    public void badRequest(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(SC_BAD_REQUEST, e.getMessage());
    }

    private void invalidateLoginSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
