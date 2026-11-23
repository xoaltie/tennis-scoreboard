package com.github.xoaltie.tennisscoreboard.domain;

import lombok.Getter;

public enum Point {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    @Getter
    private final String value;

    Point(String value) {
        this.value = value;
    }
}
