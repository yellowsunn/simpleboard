package com.yellowsunn.simpleforum.api.interceptor;

import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.domain.user.Role;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.USER_ROLE) == null ||
                session.getAttribute(SessionConst.USER_ROLE) != Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
            return false;
        }

        return true;
    }
}
