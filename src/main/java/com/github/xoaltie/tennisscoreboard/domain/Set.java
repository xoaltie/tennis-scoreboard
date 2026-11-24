package com.github.xoaltie.tennisscoreboard.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Set {
    private static final int GAMES_TO_WIN = 6;
    private static final int GAMES_FOR_TIEBREAK = 6;
    private static final int MAX_GAMES_TO_WIN = 7;
    private static final int MIN_GAME_DIFFERENCE = 2;
    private final Map<Player, Integer> games = new EnumMap<>(Player.class);
    private Game game;
    private Player winner;

    public Set() {
        this(0, 0, new RegularGame(), null);
    }

    Set(int firstPlayerGames, int secondPlayerGames, Game game, Player winner) {
        Objects.requireNonNull(game, "game must not be null");

        if (firstPlayerGames < 0 || secondPlayerGames < 0) {
            throw new IllegalArgumentException("games must not be negative");
        }

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

        if (game.getWinner().isEmpty()) {
            return;
        }

        games.put(player, games.get(player) + 1);

        int current = games.get(player);
        int opposite = games.get(player.opposite());
        if ((current == GAMES_TO_WIN && current - opposite >= MIN_GAME_DIFFERENCE) || current == MAX_GAMES_TO_WIN) {
            winner = player;
        } else if (current == GAMES_FOR_TIEBREAK && opposite == GAMES_FOR_TIEBREAK) {
            game = new Tiebreak();
        } else {
            game = new RegularGame();
        }
    }


    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public Integer getGames(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return games.get(player);
    }

    public Optional<Point> getPoints(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return game.getPoints(player);
    }

    public Optional<Integer> getTiebreakPoints(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        return game.getTiebreakPoints(player);
    }
}
