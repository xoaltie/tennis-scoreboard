package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class MatchRepositoryImpl implements MatchRepository {
    private final SessionFactory sessionFactory;

    public MatchRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Match> findAll() {
        String hql = """
                    SELECT m FROM Match m
                    JOIN FETCH m.firstPlayer
                    JOIN FETCH m.secondPlayer
                    JOIN FETCH m.winner
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Match.class)
                .getResultList();
    }

    @Override
    public List<Match> findAllByPlayerName(String playerName) {
        Objects.requireNonNull(playerName, "player name must not be null");
        String pattern = "%" + playerName + "%";
        String hql = """
                    SELECT m FROM Match m
                    JOIN FETCH m.firstPlayer
                    JOIN FETCH m.secondPlayer
                    JOIN FETCH m.winner
                    WHERE m.firstPlayer.name LIKE :pattern
                    OR m.secondPlayer.name LIKE :pattern
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Match.class)
                .setParameter("pattern", pattern)
                .getResultList();
    }
}
