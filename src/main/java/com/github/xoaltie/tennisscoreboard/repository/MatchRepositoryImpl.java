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
    public List<Match> findAll(int limit, int offset) {
        String hql = """
                    SELECT m FROM Match m
                    JOIN FETCH m.firstPlayer
                    JOIN FETCH m.secondPlayer
                    JOIN FETCH m.winner
                    ORDER BY m.id
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Match.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Match> findAllByPlayerName(String playerName, int limit, int offset) {
        Objects.requireNonNull(playerName, "player name must not be null");
        String pattern = "%" + playerName + "%";
        String hql = """
                    SELECT m FROM Match m
                    JOIN FETCH m.firstPlayer
                    JOIN FETCH m.secondPlayer
                    JOIN FETCH m.winner
                    WHERE m.firstPlayer.name LIKE :pattern
                    OR m.secondPlayer.name LIKE :pattern
                    ORDER BY m.id
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Match.class)
                .setParameter("pattern", pattern)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public int countMatches() {
        String hql = """
                    SELECT COUNT(*)
                    FROM Match m
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Long.class)
                .getSingleResult()
                .intValue();
    }

    @Override
    public int countByPlayerName(String playerName) {
        Objects.requireNonNull(playerName, "player name must not be null");
        String pattern = "%" + playerName + "%";
        String hql = """
                    SELECT COUNT(*) FROM Match m
                    WHERE m.firstPlayer.name LIKE :pattern
                    OR m.secondPlayer.name LIKE :pattern
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("pattern", pattern)
                .getSingleResult()
                .intValue();
    }

    @Override
    public void save(Match match) {
        Objects.requireNonNull(match, "match must not be null");
        sessionFactory.getCurrentSession()
                .persist(match);
    }
}
