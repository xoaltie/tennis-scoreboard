package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Match;

import java.util.List;

public interface MatchRepository {
    List<Match> findAll(int limit, int offset);

    List<Match> findAllByPlayerName(String playerName, int limit, int offset);

    int countMatches();

    int countByPlayerName(String playerName);

    void save(Match match);
}
