package com.yellowsunn.userservice.mock;

import com.yellowsunn.userservice.utils.PasswordEncoder;

public class TestPasswordEncoder implements PasswordEncoder {

    private final String password;

    public TestPasswordEncoder(String password) {
        this.password = password;
    }

    @Override
    public String encode(String rawPassword) {
        return password;
    }
}
