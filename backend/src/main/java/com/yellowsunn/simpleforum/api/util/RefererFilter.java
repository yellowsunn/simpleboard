package com.yellowsunn.simpleforum.api.util;

import com.yellowsunn.simpleforum.exception.InvalidReferException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class RefererFilter {

    @Value("${allow.origin}")
    private String origin;

    public void check(String... paths) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String refererHeader = request.getHeader("Referer");

        if (refererHeader == null) {
            throw new InvalidReferException();
        }

        List<String> equalPaths = new ArrayList<>();
        List<String> startWithPaths = new ArrayList<>();

        for (String path : paths) {
            if (path.contains("*")) {
                startWithPaths.add(path);
            } else {
                equalPaths.add(path);
            }
        }

        checkEqual(refererHeader, equalPaths);
        checkStartWith(refererHeader, startWithPaths);
    }

    private void checkEqual(String refererHeader, List<String> paths) {
        if (paths.isEmpty()) return;

        if (hasNotEqual(refererHeader, paths)) {
            throw new InvalidReferException();
        }
    }

    private boolean hasNotEqual(String refererHeader, List<String> paths) {
        for (String path : paths) {
            if (refererHeader.equals(origin + path)) {
                return false;
            }
        }
        return true;
    }

    private void checkStartWith(String refererHeader, List<String> paths) {
        if (paths.isEmpty()) return;

        if (hasNotStartWith(refererHeader, paths)) {
            throw new InvalidReferException();
        }
    }

    private boolean hasNotStartWith(String refererHeader, List<String> paths) {
        for (String path : paths) {
            if (refererHeader.startsWith(origin + path.substring(0, path.indexOf("*")))) {
                return false;
            }
        }
        return true;
    }
}
