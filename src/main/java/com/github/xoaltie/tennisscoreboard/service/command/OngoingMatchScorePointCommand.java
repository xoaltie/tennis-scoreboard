package com.github.xoaltie.tennisscoreboard.service.command;

import java.util.UUID;

public record OngoingMatchScorePointCommand(UUID id, String playerName) {
    public OngoingMatchScorePointCommand {
        playerName = playerName.trim();
    }
}
