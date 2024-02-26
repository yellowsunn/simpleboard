package com.yellowsunn.userservice.filter;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_ID;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.exception.LoginRequireException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userId = webRequest.getHeader(USER_ID);
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser != null && loginUser.required() && userId == null) {
            throw new LoginRequireException();
        }
        return userId;
    }
}
