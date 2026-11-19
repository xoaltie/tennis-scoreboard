package com.github.xoaltie.tennisscoreboard.domain;

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
        Objects.requireNonNull(firstPlayerName, "first player name must not be null");
        Objects.requireNonNull(secondPlayerName, "second player name must not be null");

        this.id = UUID.randomUUID();
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;

        this.player1Point = Point.LOVE;
        this.player2Point = Point.LOVE;
    }

    public void scorePoint(String playerName) {
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
