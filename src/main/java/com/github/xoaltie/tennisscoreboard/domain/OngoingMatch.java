package com.github.xoaltie.tennisscoreboard.domain;

import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class OngoingMatch {
    @Getter
    private final UUID id;
    @Getter
    private final String firstPlayerName;
    @Getter
    private final String secondPlayerName;
    private final Map<Player, Integer> sets = new EnumMap<>(Player.class);
    private Set set;
    private Player winner;

    public OngoingMatch(String firstPlayerName, String secondPlayerName) {
        this(firstPlayerName, secondPlayerName, 0, 0, new Set(), null);
    }

    OngoingMatch(String firstPlayerName, String secondPlayerName, int firstPlayerSets, int secondPlayerSets, Set set, Player winner) {
        Objects.requireNonNull(firstPlayerName, "first player name must not be null");
        Objects.requireNonNull(secondPlayerName, "second player name must not be null");
        Objects.requireNonNull(set, "set must not be null");

        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            throw new IdenticalPlayerNamesException("player names must be different, provided names: first player - %s, second player - %s"
                    .formatted(firstPlayerName, secondPlayerName));
        }

        this.id = UUID.randomUUID();
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.sets.put(Player.FIRST, firstPlayerSets);
        this.sets.put(Player.SECOND, secondPlayerSets);
        this.set = set;
        this.winner = winner;
    }

    public void scorePoint(String playerName) {
        Player player = resolvePlayer(playerName);

        if (winner != null) {
            return;
        }

        set.scorePoint(player);

        if (set.getWinner().isPresent()) {
            sets.replace(player, sets.get(player) + 1);
            set = new Set();
        }

        if (sets.get(player) == 2) {
            winner = player;
        }
    }

    private Player resolvePlayer(String playerName) {
        Objects.requireNonNull(playerName, "player name must not be null");
        if (playerName.equalsIgnoreCase(firstPlayerName)) {
            return Player.FIRST;
        }
        if (playerName.equalsIgnoreCase(secondPlayerName)) {
            return Player.SECOND;
        }
        throw new PlayerNotPartOfMatchException("player with name '%s' not found in match '%s'".formatted(playerName, id));
    }

    public int getSets(String playerName) {
        Player player = resolvePlayer(playerName);
        return sets.get(player);
    }

    public int getGames(String playerName) {
        Player player = resolvePlayer(playerName);
        return set.getGames(player);
    }

    public Point getPoints(String playerName) {
        Player player = resolvePlayer(playerName);
        return set.getPoints(player);
    }

    public Optional<String> getWinnerName() {
        return Optional.ofNullable(winner)
                .map(this::resolveWinnerName);
    }

    private String resolveWinnerName(Player player) {
        return player == Player.FIRST ? firstPlayerName : secondPlayerName;
    }
}
