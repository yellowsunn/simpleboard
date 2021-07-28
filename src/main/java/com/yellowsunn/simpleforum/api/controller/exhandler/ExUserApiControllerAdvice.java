package com.yellowsunn.simpleforum.api.controller.exhandler;


import com.yellowsunn.simpleforum.api.controller.UserApiController;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundUserException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@RestControllerAdvice(assignableTypes = UserApiController.class)
public class ExUserApiControllerAdvice {

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

    @ExceptionHandler(value = {PasswordMismatchException.class, ForbiddenException.class})
    public void forbidden(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(SC_FORBIDDEN, e.getMessage());
    }

    private void invalidateLoginSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
