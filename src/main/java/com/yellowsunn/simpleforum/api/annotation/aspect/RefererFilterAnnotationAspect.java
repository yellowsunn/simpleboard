package com.yellowsunn.simpleforum.api.annotation.aspect;

import com.yellowsunn.simpleforum.api.annotation.RefererFilter;
import com.yellowsunn.simpleforum.exception.InvalidReferException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RefererFilterAnnotationAspect {

    @Value("${allow.origin}")
    private String origin;

    @Before("@annotation(refererFilter)")
    public void referer(JoinPoint joinPoint, RefererFilter refererFilter) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String refererHeader = request.getHeader("Referer");
        if (refererFilter == null || refererHeader == null || !refererHeader.equals(origin + refererFilter.path())) {
            throw new InvalidReferException();
        }
    }
}
