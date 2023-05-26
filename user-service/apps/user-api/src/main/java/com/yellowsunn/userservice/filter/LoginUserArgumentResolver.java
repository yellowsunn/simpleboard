package com.yellowsunn.userservice.filter;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.exception.LoginRequireException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_ID;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long userId = toLongOrNull(webRequest.getHeader(USER_ID));
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser != null && loginUser.required() && userId == null) {
            throw new LoginRequireException();
        }
        return userId;
    }

    private Long toLongOrNull(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }
}
