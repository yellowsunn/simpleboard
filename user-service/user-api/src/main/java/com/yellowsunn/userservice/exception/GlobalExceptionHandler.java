package com.yellowsunn.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_FAILED)
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
