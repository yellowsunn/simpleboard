package com.yellowsunn.common.exception;

public class LoginUserNotFoundException extends IllegalArgumentException {
    public LoginUserNotFoundException() {
        super("Not found login user");
    }
}
