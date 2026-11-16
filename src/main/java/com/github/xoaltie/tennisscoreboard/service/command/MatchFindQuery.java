package com.github.xoaltie.tennisscoreboard.service.command;

public record MatchFindQuery(String playerName, Integer page) {

    public MatchFindQuery {
        if (page == null || page < 1) {
            page = 1;
        }
    }
}
