package com.github.xoaltie.tennisscoreboard.entity;

import com.github.xoaltie.tennisscoreboard.persistence.CitextType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(CitextType.class)
    @Column(nullable = false, unique = true, columnDefinition = "citext")
    private String name;

    public Player(String name) {
        Objects.requireNonNull(name, "player name must not be null");
        this.name = name;
    }

    protected Player() {
    }
}
