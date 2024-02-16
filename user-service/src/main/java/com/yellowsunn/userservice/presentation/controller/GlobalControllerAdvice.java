package com.yellowsunn.userservice.presentation.controller;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import com.yellowsunn.userservice.presentation.response.ErrorMessage;
import com.yellowsunn.userservice.presentation.response.ErrorResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("message={}", e.getMessage(), e);
        return ErrorResponse.fromErrorMessage(ErrorMessage.UNKNOWN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleIllegalStateException(IllegalStateException e) {
        log.warn("message={}", e.getMessage(), e);
        return ErrorResponse.fromErrorMessage(ErrorMessage.UNKNOWN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String allMessages = emptyIfNull(e.getAllErrors()).stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("Invalid request. message={}", allMessages, e);

        return ErrorResponse.fromErrorMessage(ErrorMessage.UNKNOWN);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnknownException(Exception e) {
        log.error("Unknown exception. message={}", e.getMessage(), e);
        return ErrorResponse.fromErrorMessage(ErrorMessage.UNKNOWN);
    }
}
