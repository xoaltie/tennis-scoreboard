package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.domain.OngoingMatch;
import com.github.xoaltie.tennisscoreboard.domain.Point;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchCreateRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchPointRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchCreateResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchResponseDto;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchScorePointCommand;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchPlayerResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OngoingMatchMapper {
    OngoingMatchCreateCommand toCreateCommand(OngoingMatchCreateRequestDto dto);

    OngoingMatchCreateResponseDto toCreateResponse(OngoingMatchCreateResult result);

    OngoingMatchResponseDto toResponseDto(OngoingMatchResult result);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "dto.name", target = "playerName")
    OngoingMatchScorePointCommand toPointCommand(UUID uuid, OngoingMatchPointRequestDto dto);

    default String mapPointToString(Point points) {
        return points == null ? null : points.getValue();
    }

    default OngoingMatchResult toResult(OngoingMatch match) {
        if (match == null) {
            return null;
        }

        OngoingMatchPlayerResult firstPlayer = toPlayerResult(match.getFirstPlayerName(), match);
        OngoingMatchPlayerResult secondPlayer = toPlayerResult(match.getSecondPlayerName(), match);
        Optional<String> winnerName = match.getWinnerName();

        return new OngoingMatchResult(
                firstPlayer,
                secondPlayer,
                winnerName.orElse(null)
        );
    }

    private OngoingMatchPlayerResult toPlayerResult(String playerName, OngoingMatch match) {
        return new OngoingMatchPlayerResult(
                playerName,
                match.getPoints(playerName).orElse(null),
                match.getGames(playerName),
                match.getSets(playerName),
                match.getTiebreakPoints(playerName).orElse(null)
        );
    }
}
