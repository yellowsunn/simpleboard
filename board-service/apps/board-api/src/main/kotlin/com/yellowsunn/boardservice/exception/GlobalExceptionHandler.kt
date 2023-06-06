package com.yellowsunn.boardservice.exception

import com.yellowsunn.common.exception.LoginRequireException
import com.yellowsunn.common.exception.UserNotFoundException
import com.yellowsunn.common.response.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    protected fun handleIllegalArgumentAndStateException(e: Exception): ErrorResponse {
        logger.warn("Illegal request. message={}", e.message, e)
        return ErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.name)
            .message(e.message)
            .status(HttpStatus.BAD_REQUEST.value())
            .build()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse {
        logger.warn("Invalid request. message={}", e.message, e)
        return ErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.name)
            .message(getFirstErrorMessage(e.allErrors))
            .status(HttpStatus.BAD_REQUEST.value())
            .build()
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginRequireException::class)
    protected fun handleInvalidAuthorizationException(e: LoginRequireException): ErrorResponse? {
        logger.warn("Login required. message={}", e.message, e)
        return ErrorResponse.requireLogin()
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotFoundException::class)
    protected fun handleUserNotFoundException(e: UserNotFoundException): ErrorResponse {
        logger.warn("Not found user. message={}", e.message, e)
        return ErrorResponse.notFoundUser()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    protected fun handleUnknownException(e: RuntimeException): ErrorResponse {
        logger.error("Unknown exception. message={}", e.message, e)
        return ErrorResponse.unknownError()
    }

    private fun getFirstErrorMessage(errors: List<ObjectError>?): String {
        return errors?.firstOrNull()?.defaultMessage ?: ""
    }
}
