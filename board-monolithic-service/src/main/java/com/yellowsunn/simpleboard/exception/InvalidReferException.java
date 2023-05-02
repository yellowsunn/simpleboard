package com.yellowsunn.simpleboard.exception;

public class InvalidReferException extends RuntimeException {
    public InvalidReferException() {
        super("정상적인 접근이 아닙니다.");
    }

    public InvalidReferException(String message) {
        super(message);
    }

    public InvalidReferException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReferException(Throwable cause) {
        super(cause);
    }

    protected InvalidReferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
