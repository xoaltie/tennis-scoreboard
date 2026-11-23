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

        if ((firstPlayerPoints == Point.ADVANTAGE && secondPlayerPoints != Point.FORTY)
                || (secondPlayerPoints == Point.ADVANTAGE && firstPlayerPoints != Point.FORTY)) {
            throw new IllegalArgumentException("advantage is only valid when the opponent has forty");
        }

        this.scores.put(Player.FIRST, firstPlayerPoints);
        this.scores.put(Player.SECOND, secondPlayerPoints);
        this.winner = winner;
    }

    public void scorePoint(Player player) {
        Objects.requireNonNull(player, "player must not be null");

        if (winner != null) {
            return;
        }

        Point current = scores.get(player);
        Point opposite = scores.get(player.opposite());
        switch (current) {
            case LOVE -> scores.put(player, Point.FIFTEEN);
            case FIFTEEN -> scores.put(player, Point.THIRTY);
            case THIRTY -> scores.put(player, Point.FORTY);
            case FORTY -> scoreFromForty(player, opposite);
            case ADVANTAGE -> winner = player;
        }
    }

    private void scoreFromForty(Player player, Point opposite) {
        switch (opposite) {
            case LOVE, FIFTEEN, THIRTY -> winner = player;
            case FORTY -> scores.put(player, Point.ADVANTAGE);
            case ADVANTAGE -> scores.put(player.opposite(), Point.FORTY);
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
