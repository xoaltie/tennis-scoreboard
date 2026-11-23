package com.github.xoaltie.tennisscoreboard.controller;

import com.github.xoaltie.tennisscoreboard.dto.request.MatchFindRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchCreateRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.request.OngoingMatchPointRequestDto;
import com.github.xoaltie.tennisscoreboard.dto.response.MatchPageResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchCreateResponseDto;
import com.github.xoaltie.tennisscoreboard.dto.response.OngoingMatchResponseDto;
import com.github.xoaltie.tennisscoreboard.mapper.FinishedMatchMapper;
import com.github.xoaltie.tennisscoreboard.mapper.OngoingMatchMapper;
import com.github.xoaltie.tennisscoreboard.service.FinishedMatchService;
import com.github.xoaltie.tennisscoreboard.service.OngoingMatchService;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchQuery;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchScorePointCommand;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public OngoingMatchResponseDto get(@PathVariable UUID uuid) {
        OngoingMatchQuery query = ongoingMatchMapper.toQuery(uuid);
        OngoingMatchResult match = ongoingMatchService.find(query);
        return ongoingMatchMapper.toResponseDto(match);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OngoingMatchCreateResponseDto create(@Validated @RequestBody OngoingMatchCreateRequestDto dto) {
        OngoingMatchCreateResult ongoingMatchCreateResult = ongoingMatchService.create(ongoingMatchMapper.toCreateCommand(dto));
        return ongoingMatchMapper.toCreateResponse(ongoingMatchCreateResult);
    }

    @PostMapping("/{uuid}/point")
    @ResponseStatus(HttpStatus.OK)
    public OngoingMatchResponseDto point(@PathVariable UUID uuid, @Validated @RequestBody OngoingMatchPointRequestDto dto) {
        OngoingMatchScorePointCommand command = ongoingMatchMapper.toPointCommand(uuid, dto);
        OngoingMatchResult match = ongoingMatchService.scorePoint(command);
        return ongoingMatchMapper.toResponseDto(match);
    }
}
