package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.dto.request.MatchFindRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchPageResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchResponseDto;
import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.service.command.MatchFindQuery;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinishedMatchMapper {

    MatchResponseDto toDto(MatchResult result);

    List<MatchResponseDto> toDtoList(List<MatchResult> results);

    @Mapping(source = "firstPlayer.name", target = "firstPlayerName")
    @Mapping(source = "secondPlayer.name", target = "secondPlayerName")
    @Mapping(source = "winner.name", target = "winnerName")
    MatchResult toResult(Match match);

    List<MatchResult> toResults(List<Match> matches);

    MatchPageResult toPageResult(List<MatchResult> results, int currentPage, int totalPages);

    MatchFindQuery toFindQuery(MatchFindRequestDto dto);

    @Mapping(source = "results", target = "matches")
    MatchPageResponseDto toPageResponseDto(MatchPageResult result);
}
