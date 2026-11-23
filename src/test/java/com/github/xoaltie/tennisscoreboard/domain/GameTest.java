package com.github.xoaltie.tennisscoreboard.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Game unit tests")
class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    @DisplayName("should throw exception when creating game without first player points")
    void constructor_withoutFirstPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new Game(null, Point.LOVE, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating game without second player points")
    void constructor_withoutSecondPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new Game(Point.LOVE, null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating game with both players at advantage")
    void constructor_bothPlayersAtAdvantage_throwsException() {
        assertThatThrownBy(() -> new Game(Point.ADVANTAGE, Point.ADVANTAGE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw exception when first player has advantage without opponent at forty")
    void constructor_firstPlayerAdvantageWithoutOpponentForty_throwsException() {
        assertThatThrownBy(() -> new Game(Point.ADVANTAGE, Point.LOVE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw exception when second player has advantage without opponent at forty")
    void constructor_secondPlayerAdvantageWithoutOpponentForty_throwsException() {
        assertThatThrownBy(() -> new Game(Point.THIRTY, Point.ADVANTAGE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should initialize both players to love and leave winner empty")
    void constructor_default_initializesLovePoints() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(Point.LOVE);
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
        assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.LOVE);
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
        Game game = new Game(Point.LOVE, current, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(expected);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should set winner when scoring point at game point")
    void scorePoint_atGamePoint_setsWinner() {
        Game game = new Game(Point.FORTY, Point.LOVE, null);

        game.scorePoint(Player.FIRST);

        assertThat(game.getWinner()).hasValue(Player.FIRST);
    }

    @Test
    @DisplayName("should set winner when scoring player has advantage")
    void scorePoint_fromAdvantage_setsWinner() {
        Game game = new Game(Point.ADVANTAGE, Point.FORTY, null);

        game.scorePoint(Player.FIRST);

        assertThat(game.getWinner()).hasValue(Player.FIRST);
    }

    @Test
    @DisplayName("should grant advantage when scoring point at deuce")
    void scorePoint_atDeuce_grantsAdvantage() {
        Game game = new Game(Point.FORTY, Point.FORTY, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.FORTY);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(Point.ADVANTAGE);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should return to deuce when scoring point against opponent advantage")
    void scorePoint_againstAdvantage_returnsToDeuce() {
        Game game = new Game(Point.ADVANTAGE, Point.FORTY, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.FORTY);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(Point.FORTY);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should do nothing when scoring point after game is finished")
    void scorePoint_afterGameFinished_doesNothing() {
        Game game = new Game(Point.LOVE, Point.LOVE, Player.SECOND);

        game.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(Point.LOVE);
            softly.assertThat(game.getWinner()).hasValue(Player.SECOND);
        });
    }
}
