package com.github.xoaltie.tennisscoreboard.domain;

import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class OngoingMatch {
    private final UUID id;
    private final String firstPlayerName;
    private final String secondPlayerName;
    private String winnerName;
    private Point player1Point;
    private Point player2Point;
    private int player1Game;
    private int player2Game;
    private int player1Set;
    private int player2Set;

    public OngoingMatch(String firstPlayerName, String secondPlayerName) {
        this(firstPlayerName, secondPlayerName, Point.LOVE, Point.LOVE, 0, 0, 0, 0);
    }

    OngoingMatch(String firstPlayerName, String secondPlayerName, Point player1Point, Point player2Point) {
        this(firstPlayerName, secondPlayerName, player1Point, player2Point, 0, 0, 0, 0);
    }

    OngoingMatch(String firstPlayerName, String secondPlayerName, Point player1Point, Point player2Point, int player1Game, int player2Game) {
        this(firstPlayerName, secondPlayerName, player1Point, player2Point, player1Game, player2Game, 0, 0);
    }

    OngoingMatch(String firstPlayerName, String secondPlayerName, Point player1Point, Point player2Point, int player1Game, int player2Game, int player1Set, int player2Set) {
        Objects.requireNonNull(firstPlayerName, "first player name must not be null");
        Objects.requireNonNull(secondPlayerName, "second player name must not be null");

        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            throw new IdenticalPlayerNamesException("player names must be different, provided names: first player - %s, second player - %s"
                    .formatted(firstPlayerName, secondPlayerName));
        }

        this.id = UUID.randomUUID();
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.player1Point = player1Point;
        this.player2Point = player2Point;
        this.player1Game = player1Game;
        this.player2Game = player2Game;
        this.player1Set = player1Set;
        this.player2Set = player2Set;
    }

    public void scorePoint(String playerName) {
        Objects.requireNonNull(playerName, "player name must not be null");
        validatePlayer(playerName);

        // TODO: так бл, не могу понять как это сделать сразу
        //  начисляем очки игроку playerName
        //  если очки стали LOVE - обнуляем очки у второго игрока тоже, добавляем +1 к гейму
        //  если гейм стал 6 - обнуляем у обоих игроков, делаем +1 к сетам
        //  если у одного из игроков сет стал равен 3 - записываем его в winnerName и выходим
        //  пока что умозрительно проще сделать сеты и геймы

        if (winnerName != null) {
            return;
        }

        Point previousPlayer1Point = player1Point;
        Point previousPlayer2Point = player2Point;
        if (playerName.equalsIgnoreCase(firstPlayerName)) {
            player1Point = player1Point.next();
        } else {
            player2Point = player2Point.next();
        }

        if (previousPlayer1Point == Point.FORTY && player1Point == Point.LOVE) {
            player1Game++;
            player2Point = Point.LOVE;
        }
        if (previousPlayer2Point == Point.FORTY && player2Point == Point.LOVE) {
            player2Game++;
            player1Point = Point.LOVE;
        }

        if (player1Game == 6) {
            player1Set++;
            player1Game = 0;
            player2Game = 0;
        }
        if (player2Game == 6) {
            player2Set++;
            player1Game = 0;
            player2Game = 0;
        }

        if (player1Set == 2 || player2Set == 2) {
            winnerName = playerName;
        }
    }

    private void validatePlayer(String playerName) {
        if (!playerName.equalsIgnoreCase(firstPlayerName)
                && !playerName.equalsIgnoreCase(secondPlayerName)) {
            throw new PlayerNotPartOfMatchException("player with name '%s' not found in match '%s'".formatted(playerName, id));
        }
    }

    public Optional<String> getWinnerName() {
        return Optional.ofNullable(winnerName);
    }
}
