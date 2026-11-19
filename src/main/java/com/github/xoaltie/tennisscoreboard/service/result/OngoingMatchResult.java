package com.github.xoaltie.tennisscoreboard.service.result;

public record OngoingMatchResult(OngoingMatchPlayerResult firstPlayer, OngoingMatchPlayerResult secondPlayer,
                                 String winnerName) {
}
