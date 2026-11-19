package com.github.xoaltie.tennisscoreboard.controller.handler;

import com.github.xoaltie.tennisscoreboard.dto.response.ErrorResponseDto;
import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.OngoingMatchNotFoundException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponseDto("%s: %s".formatted(e.getFieldErrors().getFirst().getField(), e.getFieldErrors().getFirst().getDefaultMessage()));
    }

    @ExceptionHandler(IdenticalPlayerNamesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleIdenticalPlayersNameException(IdenticalPlayerNamesException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler(OngoingMatchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleOngoingMatchNotFoundException(OngoingMatchNotFoundException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler(PlayerNotPartOfMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handlePlayerNotPartOfMatchException(PlayerNotPartOfMatchException e) {
        return new ErrorResponseDto(e.getMessage());
    }
}
