package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.domain.Point;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchCreateRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchPointRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchCreateResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchResponseDto;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchScorePointCommand;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}
