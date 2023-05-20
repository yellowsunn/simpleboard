package com.yellowsunn.userservice.annotation;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.exception.InvalidAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String uuid = webRequest.getHeader(USER_UUID_HEADER);
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser != null && loginUser.required() && StringUtils.isBlank(uuid)) {
            throw new InvalidAuthenticationException();
        }
        return uuid;
    }
}
