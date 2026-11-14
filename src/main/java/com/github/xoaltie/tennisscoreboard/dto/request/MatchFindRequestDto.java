package com.github.xoaltie.tennisscoreboard.dto.request;

import com.github.xoaltie.tennisscoreboard.validation.PlayerName;
import org.springframework.web.bind.annotation.BindParam;

public record MatchFindRequestDto(
        @PlayerName
        @BindParam("player_name")
        String playerName
) {
}
