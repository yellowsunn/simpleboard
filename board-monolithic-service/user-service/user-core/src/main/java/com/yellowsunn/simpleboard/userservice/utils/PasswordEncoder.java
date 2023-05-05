package com.yellowsunn.simpleboard.userservice.utils;

public interface PasswordEncoder {

    String encode(String password);

    boolean matches(String password, String encodedPassword);
}
