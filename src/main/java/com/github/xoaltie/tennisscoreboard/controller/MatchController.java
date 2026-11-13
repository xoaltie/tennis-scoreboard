package com.github.xoaltie.tennisscoreboard.controller;

import com.github.xoaltie.tennisscoreboard.dto.response.MatchListResponseDto;
import com.github.xoaltie.tennisscoreboard.mapper.MatchMapper;
import com.github.xoaltie.tennisscoreboard.service.MatchService;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final MatchMapper mapper;

    public MatchController(MatchService matchService, MatchMapper mapper) {
        this.matchService = matchService;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MatchListResponseDto getAll() {
        List<MatchResult> matches = matchService.getAll();
        return mapper.toListResponse(matches);
    }
}
