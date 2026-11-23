package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.domain.OngoingMatch;
import com.github.xoaltie.tennisscoreboard.exception.OngoingMatchNotFoundException;
import com.github.xoaltie.tennisscoreboard.mapper.OngoingMatchMapper;
import com.github.xoaltie.tennisscoreboard.service.command.MatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchQuery;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchScorePointCommand;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OngoingMatchService {
    private final FinishedMatchService finishedMatchService;
    private final OngoingMatchMapper mapper;
    private final ConcurrentHashMap<UUID, OngoingMatch> matches = new ConcurrentHashMap<>();

    public OngoingMatchService(FinishedMatchService finishedMatchService, OngoingMatchMapper mapper) {
        this.finishedMatchService = finishedMatchService;
        this.mapper = mapper;
    }

    public OngoingMatchResult find(OngoingMatchQuery query) {
        Objects.requireNonNull(query, "match query must not be null");

        OngoingMatch match = matches.get(query.id());
        if (match == null) {
            throw new OngoingMatchNotFoundException("match with uuid '%s' not found".formatted(query.id()));
        }

        synchronized (match) {
            if (match.getWinnerName().isPresent()) {
                throw new OngoingMatchNotFoundException("match with uuid '%s' not found".formatted(query.id()));
            }

            return mapper.toResult(match);
        }
    }

    public OngoingMatchCreateResult create(OngoingMatchCreateCommand command) {
        Objects.requireNonNull(command, "match create command must not be null");
        String firstPlayerName = command.firstPlayerName();
        String secondPlayerName = command.secondPlayerName();

        OngoingMatch newMatch = new OngoingMatch(firstPlayerName, secondPlayerName);
        matches.put(newMatch.getId(), newMatch);

        return new OngoingMatchCreateResult(newMatch.getId());
    }

    public OngoingMatchResult scorePoint(OngoingMatchScorePointCommand pointCommand) {
        Objects.requireNonNull(pointCommand, "match score point command must not be null");

        OngoingMatch match = matches.get(pointCommand.id());
        if (match == null) {
            throw new OngoingMatchNotFoundException("match with uuid '%s' not found".formatted(pointCommand.id()));
        }

        synchronized (match) {
            Optional<String> winnerName = match.getWinnerName();
            if (winnerName.isPresent()) {
                throw new OngoingMatchNotFoundException("match with uuid '%s' not found".formatted(pointCommand.id()));
            }

            match.scorePoint(pointCommand.playerName());

            winnerName = match.getWinnerName();
            if (winnerName.isPresent()) {
                MatchCreateCommand createCommand = new MatchCreateCommand(
                        match.getFirstPlayerName(),
                        match.getSecondPlayerName(),
                        winnerName.get()
                );
                matches.remove(pointCommand.id());
                finishedMatchService.save(createCommand);
            }

            return mapper.toResult(match);
        }
    }
}
