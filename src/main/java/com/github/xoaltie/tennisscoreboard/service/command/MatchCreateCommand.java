package com.github.xoaltie.tennisscoreboard.service.command;

public record MatchCreateCommand(String firstPlayer, String secondPlayer, String winner) {
}
