package com.yellowsunn.simpleforum.api.util;

import com.yellowsunn.simpleforum.exception.InvalidReferException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class RefererFilter {

    @Value("${allow.origin}")
    private String origin;

    public void check(String path) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String refererHeader = request.getHeader("Referer");

        if (refererHeader == null) {
            throw new InvalidReferException();
        }

        if (path.contains("*")) {
            checkStartWith(path, refererHeader);
        } else {
            checkEqual(path, refererHeader);
        }
    }

    private void checkEqual(String path, String refererHeader) {
        if (!refererHeader.equalsIgnoreCase(origin + path)) {
            throw new InvalidReferException();
        }
    }

    private void checkStartWith(String path, String refererHeader) {
        if (!refererHeader.startsWith(origin + path.substring(0, path.indexOf("*")))) {
            throw new InvalidReferException();
        }
    }
}
