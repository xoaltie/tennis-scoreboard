package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Match;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
