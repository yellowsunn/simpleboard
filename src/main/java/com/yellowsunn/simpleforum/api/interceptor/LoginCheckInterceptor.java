package com.yellowsunn.simpleforum.api.interceptor;

import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.exception.UnauthorizedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.USER_ID) == null || session.getAttribute(SessionConst.USER_ROLE) == null) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
