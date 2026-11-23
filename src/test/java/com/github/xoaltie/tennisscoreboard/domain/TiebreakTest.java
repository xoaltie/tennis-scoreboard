package com.github.xoaltie.tennisscoreboard.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tiebreak unit tests")
class TiebreakTest {
    private Tiebreak game;

    @BeforeEach
    void setUp() {
        game = new Tiebreak();
    }

    @Test
    @DisplayName("should throw exception when creating tiebreak with negative first player points")
    void constructor_negativeFirstPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new Tiebreak(-1, 0, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("should throw exception when creating tiebreak with negative second player points")
    void constructor_negativeSecondPlayerPoints_throwsException() {
        assertThatThrownBy(() -> new Tiebreak(0, -1, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("should initialize tiebreak points to zero, regular points to empty, and leave winner empty")
    void constructor_default_initializesZeroPoints() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(0);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(0);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should throw exception when scoring point without player")
    void scorePoint_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> game.scorePoint(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should increment scoring player tiebreak points without affecting opponent")
    void scorePoint_fromZero_incrementsPlayerPoints() {
        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(0);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(1);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should not set winner when reaching eight all")
    void scorePoint_atEightToSeven_levelsScoreWithoutWinner() {
        Tiebreak game = new Tiebreak(8, 7, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(8);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(8);
            softly.assertThat(game.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should set winner when reaching seven points with a two-point lead")
    void scorePoint_atSixToFive_setsWinner() {
        Tiebreak game = new Tiebreak(6, 5, null);

        game.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(7);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(5);
            softly.assertThat(game.getWinner()).hasValue(Player.FIRST);
        });
    }

    @Test
    @DisplayName("should set winner when gaining a two-point lead above seven")
    void scorePoint_atSixToSeven_setsWinner() {
        Tiebreak game = new Tiebreak(6, 7, null);

        game.scorePoint(Player.SECOND);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(6);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(8);
            softly.assertThat(game.getWinner()).hasValue(Player.SECOND);
        });
    }

    @Test
    @DisplayName("should do nothing when scoring point after tiebreak is finished")
    void scorePoint_afterTiebreakFinished_doesNothing() {
        Tiebreak game = new Tiebreak(8, 6, Player.FIRST);

        game.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(game.getTiebreakPoints(Player.FIRST)).hasValue(8);
            softly.assertThat(game.getTiebreakPoints(Player.SECOND)).hasValue(6);
            softly.assertThat(game.getPoints(Player.FIRST)).isEmpty();
            softly.assertThat(game.getPoints(Player.SECOND)).isEmpty();
            softly.assertThat(game.getWinner()).hasValue(Player.FIRST);
        });
    }
}
