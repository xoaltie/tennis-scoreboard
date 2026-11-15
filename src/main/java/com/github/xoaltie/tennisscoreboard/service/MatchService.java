package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.mapper.MatchMapper;
import com.github.xoaltie.tennisscoreboard.repository.MatchRepository;
import com.github.xoaltie.tennisscoreboard.service.command.MatchFindCommand;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MatchService {
    private static final int PAGE_SIZE = 10;
    private final MatchRepository matchRepository;
    private final MatchMapper mapper;

    public MatchService(MatchRepository matchRepository, MatchMapper mapper) {
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public MatchPageResult getAll(MatchFindCommand command) {
        Objects.requireNonNull(command, "match find command must not be null");
        List<Match> matches;

        int totalMatches;
        int page = command.page();
        int offset = (page - 1) * PAGE_SIZE;

        if (command.playerName() == null) {
            matches = matchRepository.findAll(PAGE_SIZE, offset);
            totalMatches = matchRepository.countMatches();
        } else {
            matches = matchRepository.findAllByPlayerName(command.playerName(), PAGE_SIZE, offset);
            totalMatches = matchRepository.countByPlayerName(command.playerName());
        }

        int totalPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);

        List<MatchResult> matchResults = mapper.toResults(matches);
        return mapper.toPageResult(matchResults, page, totalPages);
    }
}
