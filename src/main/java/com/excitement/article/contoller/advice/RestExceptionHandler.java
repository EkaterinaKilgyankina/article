package com.excitement.article.contoller.advice;

import com.excitement.article.contoller.dto.ApiError;
import com.excitement.article.contoller.dto.ErrorCode;
import com.excitement.article.exception.ForbiddenException;
import com.excitement.article.exception.UserNameAlreadyExistException;
import com.excitement.article.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handle(ForbiddenException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError()
                .setCode(ErrorCode.FORBIDDEN)
                .setMessage("Forbidden");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handle(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError()
                .setCode(ErrorCode.FORBIDDEN)
                .setMessage("Forbidden");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handle(UserNameAlreadyExistException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError()
                .setCode(ErrorCode.USER_NAME_ALREADY_EXIST)
                .setMessage("UserName already exist. You have to change it");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handle(UserNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError()
                .setCode(ErrorCode.USER_NOT_FOUND)
                .setMessage(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(
                        new ApiError()
                                .setCode(ErrorCode.VALIDATION_ERROR)
                                .setMessage("parameterName - [%s] wrong format" .formatted(((MethodArgumentTypeMismatchException) ex).getName()))
                );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(
                        new ApiError()
                                .setCode(ErrorCode.VALIDATION_ERROR)
                                .setMessage(
                                        ex.getBindingResult()
                                                .getFieldErrors()
                                                .stream()
                                                .findFirst()
                                                .map(e -> e.getField() + " " + e.getDefaultMessage())
                                                .orElse("error field did not detected")
                                )
                );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return new ApiError()
                .setCode(ErrorCode.INTERNAL_SERVER_ERROR)
                .setMessage(ex.getMessage());
    }
}
