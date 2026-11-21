package com.github.xoaltie.tennisscoreboard.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Set {
    private final Map<Player, Integer> games = new EnumMap<>(Player.class);
    private Game game;
    private Player winner;

    public Set() {
        this(0, 0, new Game(), null);
    }

    Set(int firstPlayerGames, int secondPlayerGames, Game game, Player winner) {
        Objects.requireNonNull(game, "game must not be null");

        this.games.put(Player.FIRST, firstPlayerGames);
        this.games.put(Player.SECOND, secondPlayerGames);
        this.game = game;
        this.winner = winner;
    }

    public void scorePoint(Player player) {
        Objects.requireNonNull(player, "player must not be null");

        if (winner != null) {
            return;
        }

        game.scorePoint(player);

        if (game.getWinner().isPresent()) {
            games.replace(player, games.get(player) + 1);
            game = new Game();
        }

        if (games.get(player) == 6) {
            winner = player;
        }
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public Integer getGames(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return games.get(player);
    }

    public Point getPoints(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return game.getPoints(player);
    }
}
