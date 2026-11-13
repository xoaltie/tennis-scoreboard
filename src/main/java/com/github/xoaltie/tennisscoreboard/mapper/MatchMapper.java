package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.dto.response.MatchListResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchResponseDto;
import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchResponseDto toDto(MatchResult matchResult);

    List<MatchResponseDto> toDtoList(List<MatchResult> matchResults);

    @Mapping(source = "firstPlayer.name", target = "firstPlayerName")
    @Mapping(source = "secondPlayer.name", target = "secondPlayerName")
    @Mapping(source = "winner.name", target = "winnerName")
    MatchResult toResult(Match match);

    List<MatchResult> toResults(List<Match> matches);

    default MatchListResponseDto toListResponse(List<MatchResult> matchesResults) {
        return new MatchListResponseDto(toDtoList(matchesResults));
    }
}
