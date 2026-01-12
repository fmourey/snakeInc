package com.snakeinc.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Score {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(name = "FK_score_player"))
    private Player player;

    private LocalDateTime playedAt;
    
    private String snake;
    private Integer score;

    public Score() {
    }

    public Score(Player player, LocalDateTime playedAt, String snake, Integer score) {
        this.player = player;
        this.playedAt = playedAt;
        this.snake = snake;
        this.score = score;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
    public String getSnake() {
        return snake;
    }
    public void setSnake(String snake) {
        this.snake = snake;
    }
    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }
}
