package com.github.xoaltie.tennisscoreboard.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("should advance scoring player points without affecting opponent")
    void scorePoint_fromLove_advancesPoints() {
        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEqualTo(Point.LOVE);
            softly.assertThat(game.getPoints(Player.SECOND)).isEqualTo(Point.FIFTEEN);
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
