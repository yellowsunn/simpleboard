package com.yellowsunn.simpleforum.config;

import com.yellowsunn.simpleforum.api.argumentresolver.LoginIdArgumentResolver;
import com.yellowsunn.simpleforum.api.interceptor.AdminCheckInterceptor;
import com.yellowsunn.simpleforum.api.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/users", "/api/users/login");

        registry.addInterceptor(new AdminCheckInterceptor())
                .order(2)
                .addPathPatterns("/api/users/{userId:\\d+}");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginIdArgumentResolver());
    }
}
