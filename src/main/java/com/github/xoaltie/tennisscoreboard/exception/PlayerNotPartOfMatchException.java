package com.github.xoaltie.tennisscoreboard.exception;

public class PlayerNotPartOfMatchException extends RuntimeException {
    public PlayerNotPartOfMatchException(String message) {
        super(message);
    }
}
