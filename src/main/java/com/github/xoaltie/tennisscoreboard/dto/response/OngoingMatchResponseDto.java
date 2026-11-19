package com.github.xoaltie.tennisscoreboard.dto.response;

public record OngoingMatchResponseDto(OngoingMatchPlayerScoresResponseDto firstPlayer,
                                      OngoingMatchPlayerScoresResponseDto secondPlayer,
                                      String winnerName) {
}
