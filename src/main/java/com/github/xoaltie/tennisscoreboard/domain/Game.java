package com.github.xoaltie.tennisscoreboard.domain;

import java.util.Optional;

public interface Game {
    void scorePoint(Player player);

    Optional<Player> getWinner();

    default Optional<Point> getPoints(Player player) {
        return Optional.empty();
    }

    default Optional<Integer> getTiebreakPoints(Player player) {
        return Optional.empty();
    }
}
