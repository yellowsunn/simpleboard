package com.yellowsunn.simpleforum.api.interceptor;

import com.yellowsunn.simpleforum.api.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.USER_ID) == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요한 접근입니다.");
            return false;
        }

        return true;
    }
}
