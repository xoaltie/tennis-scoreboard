package com.github.xoaltie.tennisscoreboard.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Tiebreak implements Game {
    private final Map<Player, Integer> scores = new EnumMap<>(Player.class);
    private Player winner;

    public Tiebreak() {
        this(0, 0, null);
    }

    Tiebreak(int firstPlayerPoints, int secondPlayerPoints, Player winner) {
        if (firstPlayerPoints < 0 || secondPlayerPoints < 0) {
            throw new IllegalArgumentException("points cannot be negative");
        }

        this.scores.put(Player.FIRST, firstPlayerPoints);
        this.scores.put(Player.SECOND, secondPlayerPoints);
        this.winner = winner;
    }

    @Override
    public void scorePoint(Player player) {
        Objects.requireNonNull(player, "player must not be null");

        if (winner != null) {
            return;
        }

        scores.put(player, scores.get(player) + 1);

        int current = scores.get(player);
        int opposite = scores.get(player.opposite());
        if (current >= 7 && current - opposite >= 2) {
            winner = player;
        }
    }

    @Override
    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    @Override
    public Optional<Integer> getTiebreakPoints(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return Optional.ofNullable(scores.get(player));
    }
}
