package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.mapper.MatchMapper;
import com.github.xoaltie.tennisscoreboard.repository.MatchRepository;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper mapper;

    public MatchService(MatchRepository matchRepository, MatchMapper mapper) {
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    @Transactional
    public List<MatchResult> getAll() {
        List<Match> matches = matchRepository.findAll();
        return mapper.toResults(matches);
    }
}
