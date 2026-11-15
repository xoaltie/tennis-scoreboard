package com.github.xoaltie.tennisscoreboard.service.command;

public record MatchFindCommand(String playerName, Integer page) {

    public MatchFindCommand {
        if (page == null || page < 1) {
            page = 1;
        }
    }
}
