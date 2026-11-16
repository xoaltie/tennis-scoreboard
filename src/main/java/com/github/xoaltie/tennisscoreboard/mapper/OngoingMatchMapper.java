package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchCreateRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchResponseDto;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OngoingMatchMapper {
    OngoingMatchCreateCommand toCreateCommand(OngoingMatchCreateRequestDto dto);

    OngoingMatchResponseDto toOngoingMatchResponse(OngoingMatchCreateResult result);
}
