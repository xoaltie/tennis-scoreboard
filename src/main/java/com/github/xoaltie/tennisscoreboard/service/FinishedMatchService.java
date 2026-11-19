package com.github.xoaltie.tennisscoreboard.service;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import com.github.xoaltie.tennisscoreboard.entity.Player;
import com.github.xoaltie.tennisscoreboard.mapper.FinishedMatchMapper;
import com.github.xoaltie.tennisscoreboard.repository.MatchRepository;
import com.github.xoaltie.tennisscoreboard.repository.PlayerRepository;
import com.github.xoaltie.tennisscoreboard.service.command.MatchCreateCommand;
import com.github.xoaltie.tennisscoreboard.service.command.MatchFindQuery;
import com.github.xoaltie.tennisscoreboard.service.result.MatchPageResult;
import com.github.xoaltie.tennisscoreboard.service.result.MatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class FinishedMatchService {
    private static final int PAGE_SIZE = 10;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final FinishedMatchMapper mapper;

    public FinishedMatchService(MatchRepository matchRepository, PlayerRepository playerRepository, FinishedMatchMapper mapper) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public MatchPageResult getAll(MatchFindQuery query) {
        Objects.requireNonNull(query, "match find query must not be null");
        List<Match> matches;

        int totalMatches;
        int page = query.page();
        int offset = (page - 1) * PAGE_SIZE;

        if (query.playerName() == null) {
            matches = matchRepository.findAll(PAGE_SIZE, offset);
            totalMatches = matchRepository.countMatches();
        } else {
            matches = matchRepository.findAllByPlayerName(query.playerName(), PAGE_SIZE, offset);
            totalMatches = matchRepository.countByPlayerName(query.playerName());
        }

        // TODO: косячок. page всегда 1, а totalPages может быть 0. нужно привести к консистентному состоянию
        int totalPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);

        List<MatchResult> matchResults = mapper.toResults(matches);
        return mapper.toPageResult(matchResults, page, totalPages);
    }

    @Transactional
    public void save(MatchCreateCommand command) {
        Objects.requireNonNull(command, "match create command must not be null");

        Player firstPlayer = findOrCreate(command.firstPlayer());
        Player secondPlayer = findOrCreate(command.secondPlayer());
        Player winner = firstPlayer.getName().equalsIgnoreCase(command.winner()) ? firstPlayer : secondPlayer;

        Match newMatch = new Match(
                firstPlayer,
                secondPlayer,
                winner
        );

        matchRepository.save(newMatch);
    }

    private Player findOrCreate(String playerName) {
        return playerRepository.findByName(playerName)
                .orElseGet(() -> {
                    Player newPlayer = new Player(playerName);
                    return playerRepository.save(newPlayer);
                });
    }
}
