package com.yellowsunn.userservice.mock;

import com.yellowsunn.userservice.application.port.out.PasswordEncoder;

public class TestPasswordEncoder implements PasswordEncoder {

    private final String password;

    public TestPasswordEncoder(String password) {
        this.password = password;
    }

    @Override
    public String encode(String rawPassword) {
        return password;
    }

    @Override
    public boolean match(String rawPassword, String encodedPassword) {
        return true;
    }
}
