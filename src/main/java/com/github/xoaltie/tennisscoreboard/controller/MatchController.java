package com.github.xoaltie.tennisscoreboard.controller;

import com.github.xoaltie.tennisscoreboard.dto.request.MatchFindRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchCreateRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchPageResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchResponseDto;
import com.github.xoaltie.tennisscoreboard.mapper.FinishedMatchMapper;
import com.github.xoaltie.tennisscoreboard.mapper.OngoingMatchMapper;
import com.github.xoaltie.tennisscoreboard.service.FinishedMatchService;
import com.github.xoaltie.tennisscoreboard.service.OngoingMatchService;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final FinishedMatchService finishedMatchService;
    private final OngoingMatchService ongoingMatchService;
    private final FinishedMatchMapper finishedMatchMapper;
    private final OngoingMatchMapper ongoingMatchMapper;

    public MatchController(FinishedMatchService finishedMatchService, OngoingMatchService ongoingMatchService, FinishedMatchMapper finishedMatchMapper, OngoingMatchMapper ongoingMatchMapper) {
        this.finishedMatchService = finishedMatchService;
        this.ongoingMatchService = ongoingMatchService;
        this.finishedMatchMapper = finishedMatchMapper;
        this.ongoingMatchMapper = ongoingMatchMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MatchPageResponseDto getAll(@Validated MatchFindRequestDto dto) {
        MatchPageResult matchPageResult = finishedMatchService.getAll(finishedMatchMapper.toFindQuery(dto));
        return finishedMatchMapper.toPageResponseDto(matchPageResult);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OngoingMatchResponseDto create(@Validated @RequestBody OngoingMatchCreateRequestDto dto) {
        OngoingMatchCreateResult ongoingMatchCreateResult = ongoingMatchService.create(ongoingMatchMapper.toCreateCommand(dto));
        return ongoingMatchMapper.toOngoingMatchResponse(ongoingMatchCreateResult);
    }
}
