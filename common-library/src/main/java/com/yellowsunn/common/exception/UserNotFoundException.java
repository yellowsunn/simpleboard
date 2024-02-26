package com.yellowsunn.common.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException() {
        super("Not found user");
    }
}
