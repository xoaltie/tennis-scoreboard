package com.github.xoaltie.tennisscoreboard.repository;

import com.github.xoaltie.tennisscoreboard.entity.Player;
import org.hibernate.SessionFactory;
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
    public Player save(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        sessionFactory.getCurrentSession()
                .persist(player);
        return player;
    }
}
