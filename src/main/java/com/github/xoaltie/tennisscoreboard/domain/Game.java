package com.github.xoaltie.tennisscoreboard.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Game {
    private final Map<Player, Point> scores = new EnumMap<>(Player.class);
    private Player winner;

    public Game() {
        this(Point.LOVE, Point.LOVE, null);
    }

    Game(Point firstPlayerPoints, Point secondPlayerPoints, Player winner) {
        Objects.requireNonNull(firstPlayerPoints, "first player points must not be null");
        Objects.requireNonNull(secondPlayerPoints, "second player points must not be null");

        this.scores.put(Player.FIRST, firstPlayerPoints);
        this.scores.put(Player.SECOND, secondPlayerPoints);
        this.winner = winner;
    }

    public void scorePoint(Player player) {
        Objects.requireNonNull(player, "player must not be null");

        if (winner != null) {
            return;
        }

        Point previous = scores.get(player);
        scores.replace(player, previous.next());
        Point current = scores.get(player);
        if (previous == Point.FORTY && current == Point.LOVE) {
            winner = player;
        }
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public Point getPoints(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return scores.get(player);
    }
}
