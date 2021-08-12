package com.yellowsunn.simpleforum.config;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.yellowsunn.simpleforum.api.argumentresolver.LoginIdArgumentResolver;
import com.yellowsunn.simpleforum.api.interceptor.AdminCheckInterceptor;
import com.yellowsunn.simpleforum.api.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Value("${allow.origin}")
    private String allowOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowOrigin)
                .allowCredentials(true);
    }

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

    //Lucy Xss filter 적용
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean(){
        FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
