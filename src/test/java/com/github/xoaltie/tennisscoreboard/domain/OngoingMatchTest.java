package com.github.xoaltie.tennisscoreboard.domain;

import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.exception.PlayerNotPartOfMatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

//    @Test
//    @DisplayName("should initialize all counters to zero and leave winner null")
//    void constructor_validPlayers_initializesZeroScore() {
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(match.getFirstPlayerName()).isEqualTo(FIRST_PLAYER_NAME);
//            softly.assertThat(match.getSecondPlayerName()).isEqualTo(SECOND_PLAYER_NAME);
//            softly.assertThat(match.getPlayer1Point()).isEqualTo(Point.LOVE);
//            softly.assertThat(match.getPlayer2Point()).isEqualTo(Point.LOVE);
//            softly.assertThat(match.getPlayer1Game()).isEqualTo(0);
//            softly.assertThat(match.getPlayer2Game()).isEqualTo(0);
//            softly.assertThat(match.getPlayer1Set()).isEqualTo(0);
//            softly.assertThat(match.getPlayer2Set()).isEqualTo(0);
//            softly.assertThat(match.getWinnerName()).isEmpty();
//        });
//    }

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

//    @Test
//    @DisplayName("should advance player point from LOVE to FIFTEEN on first scored point")
//    void scorePoint_onLove_advancesToFifteen() {
//        match.scorePoint(FIRST_PLAYER_NAME);
//        assertThat(match.getPlayer1Point()).isEqualTo(Point.FIFTEEN);
//        assertThat(match.getPlayer2Point()).isEqualTo(Point.LOVE);
//    }
//
//    @Test
//    @DisplayName("should award game and reset points when player at FORTY scores")
//    void scorePoint_atForty_winsGameAndResetsPoints() {
//        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, Point.FORTY, Point.LOVE);
//        match.scorePoint(FIRST_PLAYER_NAME);
//        assertThat(match.getPlayer1Point()).isEqualTo(Point.LOVE);
//        assertThat(match.getPlayer2Point()).isEqualTo(Point.LOVE);
//        assertThat(match.getPlayer1Game()).isEqualTo(1);
//        assertThat(match.getPlayer2Game()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("should award set and reset games when leading player wins deciding game")
//    void scorePoint_atSetPoint_winsSetAndResetsGames() {
//        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, Point.FORTY, Point.LOVE, 5, 0);
//        match.scorePoint(FIRST_PLAYER_NAME);
//        assertThat(match.getPlayer1Game()).isEqualTo(0);
//        assertThat(match.getPlayer2Game()).isEqualTo(0);
//        assertThat(match.getPlayer1Set()).isEqualTo(1);
//        assertThat(match.getPlayer2Set()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("should crown winner when player wins required two sets")
//    void scorePoint_atMatchPoint_setsWinner() {
//        OngoingMatch match = new OngoingMatch(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, Point.FORTY, Point.LOVE, 5, 0, 1, 0);
//        match.scorePoint(FIRST_PLAYER_NAME);
//        assertThat(match.getWinnerName()).hasValue(FIRST_PLAYER_NAME);
//    }
}
