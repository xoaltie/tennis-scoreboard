package com.github.xoaltie.tennisscoreboard.domain;

import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OngoingMatch unit tests")
class OngoingMatchTest {
    private static final String FIRST_PLAYER_NAME = "Peter";
    private static final String SECOND_PLAYER_NAME = "John";
    private static final String UNKNOWN_PLAYER_NAME = "Bob";
    private OngoingMatch match;

    @BeforeEach
    void setUp() {
        match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME);
    }

    @Test
    @DisplayName("should throw exception when creating match without first player name")
    void constructor_withoutFirstPlayerName_throwsException() {
        assertThatThrownBy(() -> new OngoingMatch(null, SECOND_PLAYER_NAME))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating match without second player name")
    void constructor_withoutSecondPlayerName_throwsException() {
        assertThatThrownBy(() -> new OngoingMatch(FIRST_PLAYER_NAME, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when creating match with identical player names")
    void constructor_identicalPlayerNames_throwsException() {
        assertThatThrownBy(() -> new OngoingMatch(FIRST_PLAYER_NAME, FIRST_PLAYER_NAME))
                .isInstanceOf(IdenticalPlayerNamesException.class);
    }

    @Test
    @DisplayName("should initialize all counters to zero and leave winner null")
    void constructor_validPlayers_initializesZeroScore() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(match.getFirstPlayerName()).isEqualTo(FIRST_PLAYER_NAME);
            softly.assertThat(match.getSecondPlayerName()).isEqualTo(SECOND_PLAYER_NAME);
            softly.assertThat(match.getPoints(FIRST_PLAYER_NAME)).hasValue(Point.LOVE);
            softly.assertThat(match.getPoints(SECOND_PLAYER_NAME)).hasValue(Point.LOVE);
            softly.assertThat(match.getGames(FIRST_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getGames(SECOND_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getSets(FIRST_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getSets(SECOND_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getTiebreakPoints(FIRST_PLAYER_NAME)).isEmpty();
            softly.assertThat(match.getTiebreakPoints(SECOND_PLAYER_NAME)).isEmpty();
            softly.assertThat(match.getWinnerName()).isEmpty();
        });
    }

    @Test
    @DisplayName("should throw exception when player name is null")
    void scorePoint_nullPlayerName_throwsException() {
        assertThatThrownBy(() -> match.scorePoint(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when scoring point for player not in match")
    void scorePoint_unknownPlayer_throwsException() {
        assertThatThrownBy(() -> match.scorePoint(UNKNOWN_PLAYER_NAME))
                .isInstanceOf(PlayerNotPartOfMatchException.class);
    }

    @Test
    @DisplayName("should throw exception when get sets without player name")
    void getSets_withoutPlayerName_throwsException() {
        assertThatThrownBy(() -> match.getSets(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when get sets for player not in match")
    void getSets_unknownPlayer_throwsException() {
        assertThatThrownBy(() -> match.getSets(UNKNOWN_PLAYER_NAME))
                .isInstanceOf(PlayerNotPartOfMatchException.class);
    }

    @Test
    @DisplayName("should return player sets")
    void getSets_validPlayer_returnsSets() {
        assertThat(match.getSets(SECOND_PLAYER_NAME)).isEqualTo(0);
    }

    @Test
    @DisplayName("should throw exception when get games without player name")
    void getGames_withoutPlayerName_throwsException() {
        assertThatThrownBy(() -> match.getGames(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when get games for player not in match")
    void getGames_unknownPlayer_throwsException() {
        assertThatThrownBy(() -> match.getGames(UNKNOWN_PLAYER_NAME))
                .isInstanceOf(PlayerNotPartOfMatchException.class);
    }

    @Test
    @DisplayName("should return player games")
    void getGames_validPlayer_returnsGames() {
        assertThat(match.getGames(SECOND_PLAYER_NAME)).isEqualTo(0);
    }

    @Test
    @DisplayName("should throw exception when get points without player name")
    void getPoints_withoutPlayerName_throwsException() {
        assertThatThrownBy(() -> match.getPoints(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when get points for player not in match")
    void getPoints_unknownPlayer_throwsException() {
        assertThatThrownBy(() -> match.getPoints(UNKNOWN_PLAYER_NAME))
                .isInstanceOf(PlayerNotPartOfMatchException.class);
    }

    @Test
    @DisplayName("should return player points")
    void getPoints_validPlayer_returnsPoints() {
        assertThat(match.getPoints(SECOND_PLAYER_NAME)).hasValue(Point.LOVE);
    }

    @Test
    @DisplayName("should award set and reset games when leading player wins deciding game")
    void scorePoint_atSetPoint_winsSetAndResetsGames() {
        RegularGame game = new RegularGame(Point.FORTY, Point.LOVE, null);
        Set set = new Set(5, 0, game, null);
        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, 0, 0, set, null);

        match.scorePoint(FIRST_PLAYER_NAME);

        assertThat(match.getGames(FIRST_PLAYER_NAME)).isEqualTo(0);
        assertThat(match.getGames(SECOND_PLAYER_NAME)).isEqualTo(0);
        assertThat(match.getSets(FIRST_PLAYER_NAME)).isEqualTo(1);
        assertThat(match.getSets(SECOND_PLAYER_NAME)).isEqualTo(0);
    }

    @Test
    @DisplayName("should crown winner when player wins required two sets")
    void scorePoint_atMatchPoint_setsWinner() {
        RegularGame game = new RegularGame(Point.FORTY, Point.LOVE, null);
        Set set = new Set(5, 0, game, null);
        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, 1, 0, set, null);

        match.scorePoint(FIRST_PLAYER_NAME);

        assertThat(match.getWinnerName()).hasValue(FIRST_PLAYER_NAME);
    }

    @Test
    @DisplayName("should do nothing when scoring a point after match is finished")
    void scorePoint_afterMatchFinished_doesNothing() {
        RegularGame game = new RegularGame(Point.LOVE, Point.LOVE, null);
        Set set = new Set(0, 0, game, null);
        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, 2, 0, set, Player.FIRST);

        match.scorePoint(FIRST_PLAYER_NAME);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(match.getPoints(FIRST_PLAYER_NAME)).hasValue(Point.LOVE);
            softly.assertThat(match.getPoints(SECOND_PLAYER_NAME)).hasValue(Point.LOVE);
            softly.assertThat(match.getGames(FIRST_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getGames(SECOND_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getSets(FIRST_PLAYER_NAME)).isEqualTo(2);
            softly.assertThat(match.getSets(SECOND_PLAYER_NAME)).isEqualTo(0);
            softly.assertThat(match.getTiebreakPoints(FIRST_PLAYER_NAME)).isEmpty();
            softly.assertThat(match.getTiebreakPoints(SECOND_PLAYER_NAME)).isEmpty();
            softly.assertThat(match.getWinnerName()).hasValue(FIRST_PLAYER_NAME);
        });
    }
}
