package com.yellowsunn.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomUserException.class)
    protected ResponseEntity<ErrorResponse> handleCustomUserException(CustomUserException e) {
        var errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getDescription())
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Invalid request. message={}", e.getMessage(), e);
        return ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(getFirstErrorMessage(e.getAllErrors()))
                .build();
    }

    private String getFirstErrorMessage(List<ObjectError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            return "";
        }
        return errors.get(0).getDefaultMessage();
    }
}
