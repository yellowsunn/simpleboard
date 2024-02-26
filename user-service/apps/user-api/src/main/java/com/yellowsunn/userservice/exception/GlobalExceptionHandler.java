package com.yellowsunn.userservice.exception;

import com.yellowsunn.common.exception.LoginRequireException;
import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.common.response.ResultResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    protected ResultResponse<Void> handleIllegalArgumentAndStateException(Exception e) {
        log.warn("Illegal request. message={}", e.getMessage(), e);
        return ResultResponse.failed(HttpStatus.BAD_REQUEST.name(), e.getMessage());
    }

    @ExceptionHandler(CustomUserException.class)
    protected ResponseEntity<ResultResponse<Void>> handleCustomUserException(CustomUserException e) {
        var errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ResultResponse.failed(errorCode.name(), errorCode.getDescription()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResultResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Invalid request. message={}", e.getMessage(), e);
        return ResultResponse.failed(HttpStatus.BAD_REQUEST.name(), getFirstErrorMessage(e.getAllErrors()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginRequireException.class)
    protected ResultResponse<Void> handleInvalidAuthorizationException(LoginRequireException e) {
        log.warn("Login required. message={}", e.getMessage(), e);
        return ResultResponse.requireLogin();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotFoundException.class)
    protected ResultResponse<Void> handleUserNotFoundException(UserNotFoundException e) {
        log.warn("Not found user. message={}", e.getMessage(), e);
        return ResultResponse.notFoundLoginUser();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ResultResponse<Void> handleUnknownException(Exception e) {
        log.error("Unknown exception. message={}", e.getMessage(), e);
        return ResultResponse.unknownError();
    }

    private String getFirstErrorMessage(List<ObjectError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            return "";
        }
        return errors.get(0).getDefaultMessage();
    }
}
