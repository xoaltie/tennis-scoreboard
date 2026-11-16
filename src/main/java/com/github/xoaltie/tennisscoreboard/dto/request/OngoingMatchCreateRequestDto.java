package com.github.xoaltie.tennisscoreboard.dto.request;

import com.github.xoaltie.tennisscoreboard.validation.PlayerName;
import jakarta.validation.constraints.NotBlank;

public record OngoingMatchCreateRequestDto(
        @NotBlank
        @PlayerName
        String firstPlayerName,
        @NotBlank
        @PlayerName
        String secondPlayerName) {
}
