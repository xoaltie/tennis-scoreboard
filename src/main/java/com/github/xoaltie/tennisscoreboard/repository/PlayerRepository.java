package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findByName(String name);

    Player findOrCreate(String name);
}
