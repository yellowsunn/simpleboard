package com.yellowsunn.userservice.oauth2.config;

import com.yellowsunn.userservice.oauth2.handler.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2LoginSuccessHandler successHandler) throws Exception {
        return http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .headers(it -> it.frameOptions().disable())
                .oauth2Login(it -> it.successHandler(successHandler))
                .build();
    }
}
