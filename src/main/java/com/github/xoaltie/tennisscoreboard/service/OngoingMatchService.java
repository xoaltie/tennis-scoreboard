package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.domain.OngoingMatch;
import com.github.xoaltie.tennisscoreboard.exception.IdenticalPlayerNamesException;
import com.github.xoaltie.tennisscoreboard.mapper.OngoingMatchMapper;
import com.github.xoaltie.tennisscoreboard.service.command.OngoingMatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.result.OngoingMatchCreateResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OngoingMatchService {
    private final OngoingMatchMapper mapper;
    private final ConcurrentHashMap<UUID, OngoingMatch> matches = new ConcurrentHashMap<>();

    public OngoingMatchService(OngoingMatchMapper mapper) {
        this.mapper = mapper;
    }

    public OngoingMatchCreateResult create(OngoingMatchCreateCommand command) {
        Objects.requireNonNull(command, "match create command must not be null");
        String firstPlayerName = command.firstPlayerName();
        String secondPlayerName = command.secondPlayerName();

        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            throw new IdenticalPlayerNamesException("player names must be different, provided names: first player - %s, second player - %s"
                    .formatted(firstPlayerName, secondPlayerName));
        }

        OngoingMatch newMatch = new OngoingMatch();
        matches.put(newMatch.getId(), newMatch);

        return new OngoingMatchCreateResult(newMatch.getId());
    }
}
