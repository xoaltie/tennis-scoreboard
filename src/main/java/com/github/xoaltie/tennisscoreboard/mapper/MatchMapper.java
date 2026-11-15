package com.github.xoaltie.tennisscoreboard.mapper;

import com.github.xoaltie.tennisscoreboard.dto.request.MatchFindRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchPageResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchResponseDto;
import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.service.command.MatchFindCommand;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
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

    MatchPageResult toPageResult(List<MatchResult> matchResults, int currentPage, int totalPages);

    MatchFindCommand toCommand(MatchFindRequestDto dto);

    @Mapping(source = "matchResults", target = "matches")
    MatchPageResponseDto toPageResponseDto(MatchPageResult matchPageResult);

//    default MatchPageResponseDto toPageResponse(MatchPageResult matchPageResult) {
//        return new MatchPageResponseDto(toDtoList(matchesResults));
//    }
}
