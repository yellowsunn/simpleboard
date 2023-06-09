package com.yellowsunn.notificationservice.exception

import com.yellowsunn.common.exception.LoginRequireException
import com.yellowsunn.common.exception.LoginUserNotFoundException
import com.yellowsunn.common.response.ResultResponse
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
    protected fun handleIllegalArgumentAndStateException(e: Exception): ResultResponse<Void> {
        logger.warn("Illegal request. message={}", e.message, e)
        return ResultResponse.failed(HttpStatus.BAD_REQUEST.name, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResultResponse<Void> {
        logger.warn("Invalid request. message={}", e.message, e)
        return ResultResponse.failed(HttpStatus.BAD_REQUEST.name, getFirstErrorMessage(e.allErrors))
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginRequireException::class)
    protected fun handleInvalidAuthorizationException(e: LoginRequireException): ResultResponse<Void> {
        logger.warn("Login required. message={}", e.message, e)
        return ResultResponse.requireLogin()
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginUserNotFoundException::class)
    protected fun handleUserNotFoundException(e: LoginUserNotFoundException): ResultResponse<Void> {
        logger.warn("Not found user. message={}", e.message, e)
        return ResultResponse.notFoundLoginUser()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    protected fun handleUnknownException(e: RuntimeException): ResultResponse<Void> {
        logger.error("Unknown exception. message={}", e.message, e)
        return ResultResponse.unknownError()
    }

    private fun getFirstErrorMessage(errors: List<ObjectError>?): String {
        return errors?.firstOrNull()?.defaultMessage ?: ""
    }
}
