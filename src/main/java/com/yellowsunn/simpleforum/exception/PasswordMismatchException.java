package com.yellowsunn.simpleforum.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("패스워드가 일치하지 않습니다.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }

    protected PasswordMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
