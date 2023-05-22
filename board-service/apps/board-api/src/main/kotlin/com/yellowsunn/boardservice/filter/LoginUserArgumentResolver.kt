package com.yellowsunn.boardservice.filter

import com.yellowsunn.common.annotation.LoginUser
import com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER
import com.yellowsunn.common.exception.InvalidAuthenticationException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class LoginUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val uuid: String? = webRequest.getHeader(USER_UUID_HEADER)
        val loginUser: LoginUser? = parameter.getParameterAnnotation(LoginUser::class.java)
        if (loginUser?.required == true && uuid.isNullOrBlank()) {
            throw InvalidAuthenticationException()
        }
        return uuid
    }
}
