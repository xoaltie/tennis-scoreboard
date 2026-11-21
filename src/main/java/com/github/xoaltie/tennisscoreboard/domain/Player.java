package com.github.xoaltie.tennisscoreboard.domain;

public enum Player {
    FIRST,
    SECOND;

    public Player opposite() {
        return this == FIRST ? SECOND : FIRST;
    }
}
