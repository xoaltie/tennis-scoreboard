package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Player;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {
    private final SessionFactory sessionFactory;

    public PlayerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Player> findByName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        String hql = """
                SELECT p FROM Player p
                WHERE p.name = :name
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }

    @Override
    public Player findOrCreate(String name) {
        Objects.requireNonNull(name, "name must not be null");

        String sql = """
                INSERT INTO players (name) VALUES (:name)
                ON CONFLICT (name) DO NOTHING
                """;
        sessionFactory.getCurrentSession()
                .createNativeMutationQuery(sql)
                .setParameter("name", name)
                .executeUpdate();

        Optional<Player> savedPlayer = findByName(name);
        return savedPlayer
                .orElseThrow(() -> new DataRetrievalFailureException("failed to retrieve player '%s' after insert".formatted(name)));
    }
}
