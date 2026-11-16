package com.github.xoaltie.tennisscoreboard.domain;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class OngoingMatch {
    private final UUID id;

    public OngoingMatch() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}
