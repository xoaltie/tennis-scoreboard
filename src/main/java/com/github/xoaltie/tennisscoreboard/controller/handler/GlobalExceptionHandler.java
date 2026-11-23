package com.github.xoaltie.tennisscoreboard.controller.handler;

import com.github.xoaltie.tennisscoreboard.dto.response.ErrorResponseDto;
import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.OngoingMatchNotFoundException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("invalid json body", e);
        return new ErrorResponseDto("invalid json body");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("invalid parameter format", e);
        return new ErrorResponseDto("invalid parameter format");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponseDto("%s: %s".formatted(e.getFieldErrors().getFirst().getField(), e.getFieldErrors().getFirst().getDefaultMessage()));
    }

    @ExceptionHandler({
            IdenticalPlayerNamesException.class,
            PlayerNotPartOfMatchException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMatchDomainBadRequest(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(Exception e) {
        log.warn("resource not found", e);
        return new ErrorResponseDto("resource not found");
    }

    @ExceptionHandler(OngoingMatchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleOngoingMatchNotFoundException(OngoingMatchNotFoundException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponseDto handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("method not supported", e);
        return new ErrorResponseDto("method not supported");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(Exception e) {
        log.error("unhandled exception", e);
        return new ErrorResponseDto("internal server error");
    }
}
