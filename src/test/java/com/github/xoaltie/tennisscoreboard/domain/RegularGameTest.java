package com.github.xoaltie.tennisscoreboard.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Regular game unit tests")
class RegularGameTest {
    private RegularGame game;

    @BeforeEach
    void setUp() {
        game = new RegularGame();
    }

    @Test
    @DisplayName("should throw exception when creating game without first player points")
    void constructor_withoutFirstPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new RegularGame(null, Point.LOVE, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating game without second player points")
    void constructor_withoutSecondPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new RegularGame(Point.LOVE, null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating game with both players at advantage")
    void constructor_bothPlayersAtAdvantage_throwsException() {
        assertThatThrownBy(() -> new RegularGame(Point.ADVANTAGE, Point.ADVANTAGE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw exception when first player has advantage without opponent at forty")
    void constructor_firstPlayerAdvantageWithoutOpponentForty_throwsException() {
        assertThatThrownBy(() -> new RegularGame(Point.ADVANTAGE, Point.LOVE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw exception when second player has advantage without opponent at forty")
    void constructor_secondPlayerAdvantageWithoutOpponentForty_throwsException() {
        assertThatThrownBy(() -> new RegularGame(Point.THIRTY, Point.ADVANTAGE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should initialize both players to love and leave winner empty")
    void constructor_default_initializesLovePoints() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).hasValue(Point.LOVE);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should throw exception when getting points without player")
    void getPoints_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> game.getPoints(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should return current points for player")
    void getPoints_validPlayer_returnsPoints() {
        assertThat(game.getPoints(Player.FIRST)).hasValue(Point.LOVE);
    }

    @Test
    @DisplayName("should throw exception when scoring point without player")
    void scorePoint_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> game.scorePoint(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "LOVE, FIFTEEN",
            "FIFTEEN, THIRTY",
            "THIRTY, FORTY"
    })
    @DisplayName("should advance scoring player points without affecting opponent")
    void scorePoint_beforeForty_advancesPoints(Point current, Point expected) {
        RegularGame game = new RegularGame(Point.LOVE, current, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).hasValue(expected);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should set winner when scoring point at game point")
    void scorePoint_atGamePoint_setsWinner() {
        RegularGame game = new RegularGame(Point.FORTY, Point.LOVE, null);

        game.scorePoint(Player.FIRST);

        assertThat(game.getWinner()).hasValue(Player.FIRST);
    }

    @Test
    @DisplayName("should set winner when scoring player has advantage")
    void scorePoint_fromAdvantage_setsWinner() {
        RegularGame game = new RegularGame(Point.ADVANTAGE, Point.FORTY, null);

        game.scorePoint(Player.FIRST);

        assertThat(game.getWinner()).hasValue(Player.FIRST);
    }

    @Test
    @DisplayName("should grant advantage when scoring point at deuce")
    void scorePoint_atDeuce_grantsAdvantage() {
        RegularGame game = new RegularGame(Point.FORTY, Point.FORTY, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).hasValue(Point.FORTY);
            softly.assertThat(game.getPoints(Player.SECOND)).hasValue(Point.ADVANTAGE);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should return to deuce when scoring point against opponent advantage")
    void scorePoint_againstAdvantage_returnsToDeuce() {
        RegularGame game = new RegularGame(Point.ADVANTAGE, Point.FORTY, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).hasValue(Point.FORTY);
            softly.assertThat(game.getPoints(Player.SECOND)).hasValue(Point.FORTY);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should do nothing when scoring point after game is finished")
    void scorePoint_afterGameFinished_doesNothing() {
        RegularGame game = new RegularGame(Point.LOVE, Point.LOVE, Player.SECOND);

        game.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).hasValue(Point.LOVE);
            softly.assertThat(game.getWinner()).hasValue(Player.SECOND);
        });
    }
}
