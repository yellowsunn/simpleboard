package com.yellowsunn.simpleboard.api.interceptor;

import com.yellowsunn.common.constant.SessionConst;
import com.yellowsunn.simpleboard.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.USER_ID) == null || session.getAttribute(SessionConst.USER_ROLE) == null) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
