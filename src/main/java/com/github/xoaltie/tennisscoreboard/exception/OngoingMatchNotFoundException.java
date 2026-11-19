package com.github.xoaltie.tennisscoreboard.exception;

public class OngoingMatchNotFoundException extends RuntimeException {
    public OngoingMatchNotFoundException(String message) {
        super(message);
    }
}
