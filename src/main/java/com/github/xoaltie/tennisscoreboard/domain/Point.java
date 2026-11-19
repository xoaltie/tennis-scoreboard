package com.github.xoaltie.tennisscoreboard.domain;

import lombok.Getter;

public enum Point {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40");

    @Getter
    private final String value;

    Point(String value) {
        this.value = value;
    }

    public Point next() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> LOVE;
        };
    }
}
