package com.yellowsunn.userservice.application.port.out;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean match(String rawPassword, String encodedPassword);
}
