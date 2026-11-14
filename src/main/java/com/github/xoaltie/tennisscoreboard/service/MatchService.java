package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.mapper.MatchMapper;
import com.github.xoaltie.tennisscoreboard.repository.MatchRepository;
import com.github.xoaltie.tennisscoreboard.service.command.MatchFindCommand;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper mapper;

    public MatchService(MatchRepository matchRepository, MatchMapper mapper) {
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<MatchResult> getAll(MatchFindCommand command) {
        Objects.requireNonNull(command, "match find command must not be null");
        List<Match> matches;

        if (command.playerName() == null) {
            matches = matchRepository.findAll();
        } else {
            matches = matchRepository.findAllByPlayerName(command.playerName());
        }

        return mapper.toResults(matches);
    }
}
