package com.github.xoaltie.tennisscoreboard.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Player unit tests")
class PlayerTest {
    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "FIRST, SECOND",
            "SECOND, FIRST"
    })
    @DisplayName("should return opposite player")
    void opposite_fromCurrentPlayer_returnsOppositePlayer(Player player, Player opposite) {
        assertThat(player.opposite()).isEqualTo(opposite);
    }
}
