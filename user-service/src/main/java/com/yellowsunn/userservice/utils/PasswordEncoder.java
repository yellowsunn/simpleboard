package com.yellowsunn.userservice.utils;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean match(String rawPassword, String encodedPassword);
}
