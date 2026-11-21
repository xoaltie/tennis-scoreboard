package com.github.xoaltie.tennisscoreboard.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Point unit tests")
class PointTest {
    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "LOVE, FIFTEEN",
            "FIFTEEN, THIRTY",
            "THIRTY, FORTY",
            "FORTY, LOVE"
    })
    @DisplayName("should return next point in standard sequence")
    void next_fromCurrentPoint_returnsNextPoint(Point current, Point next) {
        assertThat(current.next()).isEqualTo(next);
    }
}
