package com.yellowsunn.userservice.oauth2.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER;
import static com.yellowsunn.userservice.oauth2.constant.HeaderConst.OAUTH2_LINK;
import static com.yellowsunn.userservice.oauth2.constant.SessionConst.OAUTH2_LINK_USER;

@Component
public class BeforeOAuth2AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;

        if (isOAuth2Link(httpServletRequest)) {
            var httpSession = httpServletRequest.getSession(true);
            String uuid = httpServletRequest.getHeader(USER_UUID_HEADER);
            httpSession.setAttribute(OAUTH2_LINK_USER, StringUtils.defaultString(uuid));
        }
        chain.doFilter(request, response);
    }

    private boolean isOAuth2Link(HttpServletRequest request) {
        return StringUtils.startsWith(request.getRequestURI(), "/oauth2/authorization/")
                && StringUtils.equals(request.getHeader(OAUTH2_LINK), "true");
    }
}
