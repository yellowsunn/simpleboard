package com.yellowsunn.simpleboard.api.interceptor;

import com.yellowsunn.simpleboard.api.SessionConst;
import com.yellowsunn.simpleboard.domain.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

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
