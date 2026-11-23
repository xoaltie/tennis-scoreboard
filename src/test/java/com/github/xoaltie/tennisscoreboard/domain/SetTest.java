package com.github.xoaltie.tennisscoreboard.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Set unit tests")
class SetTest {
    private Set set;

    @BeforeEach
    void setUp() {
        set = new Set();
    }

    @Test
    @DisplayName("should throw exception when creating set without game")
    void constructor_withoutGame_throwsException() {
        assertThatThrownBy(() -> new Set(0, 0, null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should initialize games to zero, points to love, and leave winner empty")
    void constructor_default_initializesZeroScore() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(set.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(set.getPoints(Player.SECOND)).hasValue(Point.LOVE);
            softly.assertThat(set.getGames(Player.FIRST)).isEqualTo(0);
            softly.assertThat(set.getGames(Player.SECOND)).isEqualTo(0);
            softly.assertThat(set.getTiebreakPoints(Player.FIRST)).isEmpty();
            softly.assertThat(set.getTiebreakPoints(Player.SECOND)).isEmpty();
            softly.assertThat(set.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should throw exception when getting points without player")
    void getPoints_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> set.getPoints(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should return current points for player")
    void getPoints_validPlayer_returnsPoints() {
        assertThat(set.getPoints(Player.SECOND)).hasValue(Point.LOVE);
    }

    @Test
    @DisplayName("should throw exception when getting games without player")
    void getGames_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> set.getGames(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should return current games for player")
    void getGames_validPlayer_returnsGames() {
        assertThat(set.getGames(Player.SECOND)).isEqualTo(0);
    }

    @Test
    @DisplayName("should throw exception when scoring point without player")
    void scorePoint_withoutPlayer_throwsException() {
        assertThatThrownBy(() -> set.scorePoint(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should award game and reset points when player wins game")
    void scorePoint_atGamePoint_winsGameAndResetsPoints() {
        RegularGame game = new RegularGame(Point.FORTY, Point.LOVE, null);
        Set set = new Set(0, 0, game, null);

        set.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(set.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(set.getPoints(Player.SECOND)).hasValue(Point.LOVE);
            softly.assertThat(set.getGames(Player.FIRST)).isEqualTo(1);
            softly.assertThat(set.getGames(Player.SECOND)).isEqualTo(0);
            softly.assertThat(set.getTiebreakPoints(Player.FIRST)).isEmpty();
            softly.assertThat(set.getTiebreakPoints(Player.SECOND)).isEmpty();
            softly.assertThat(set.getWinner()).isEmpty();
        });
    }

    @Test
    @DisplayName("should set winner when scoring point at set point")
    void scorePoint_atSetPoint_setsWinner() {
        RegularGame game = new RegularGame(Point.FORTY, Point.LOVE, null);
        Set set = new Set(5, 0, game, null);

        set.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(set.getGames(Player.FIRST)).isEqualTo(6);
            softly.assertThat(set.getWinner()).hasValue(Player.FIRST);
        });
    }

    @Test
    @DisplayName("should do nothing when scoring point after set is finished")
    void scorePoint_afterSetFinished_doesNothing() {
        Set set = new Set(0, 6, new RegularGame(), Player.SECOND);

        set.scorePoint(Player.FIRST);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(set.getPoints(Player.FIRST)).hasValue(Point.LOVE);
            softly.assertThat(set.getPoints(Player.SECOND)).hasValue(Point.LOVE);
            softly.assertThat(set.getGames(Player.FIRST)).isEqualTo(0);
            softly.assertThat(set.getGames(Player.SECOND)).isEqualTo(6);
            softly.assertThat(set.getTiebreakPoints(Player.FIRST)).isEmpty();
            softly.assertThat(set.getTiebreakPoints(Player.SECOND)).isEmpty();
            softly.assertThat(set.getWinner()).hasValue(Player.SECOND);
        });
    }
}
