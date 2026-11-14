package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Match;

import java.util.List;

public interface MatchRepository {
    List<Match> findAll();

    List<Match> findAllByPlayerName(String playerName);
}
