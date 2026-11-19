package com.github.xoaltie.tennisscoreboard.service.result;

import com.github.xoaltie.tennisscoreboard.domain.Point;

public record OngoingMatchPlayerResult(String name, Point points, int games, int sets) {
}
