package com.snakeinc.api.service;

import org.springframework.stereotype.Service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.snakeinc.api.model.Player;
import com.snakeinc.api.repository.PlayerRepository;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerResponse createPlayer(PlayerParams params) {
        String category = params.age < 25 ? "JUNIOR" : "SENIOR";
        Player player = new Player(params.name, params.age, category, LocalDate.now());
        Player savedPlayer = playerRepository.save(player);
        return new PlayerResponse(savedPlayer.getId(), savedPlayer.getName(), savedPlayer.getAge(), 
                                 savedPlayer.getCategory(), savedPlayer.getCreatedAt());
    }


    public PlayerResponse getPlayerById(int id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            Player p = player.get();
            return new PlayerResponse(p.getId(), p.getName(), p.getAge(), p.getCategory(), p.getCreatedAt());
        }
        return null;
    }

    public PlayerResponse getPlayerByName(String name) {
        Iterable<Player> allPlayers = playerRepository.findAll();
        for (Player player : allPlayers) {
            if (player.getName().equalsIgnoreCase(name)) {
                return new PlayerResponse(player.getId(), player.getName(), player.getAge(), player.getCategory(), player.getCreatedAt());
            }
        }
        throw new IllegalArgumentException("Player with name '" + name + "' not found");
    }

    public static class PlayerParams {
        @NotNull(message = "Name cannot be null")
        public String name;
        
        @Min(value = 13, message = "Age must be greater than 13")
        public int age;
    }

    public record PlayerResponse(int id, String name, int age, String category, LocalDate createdAt) {

    }
}

