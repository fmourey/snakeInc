package com.snakeinc.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlayerService {

    private final Map<Integer, PlayerResponse> players = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public PlayerResponse createPlayer(PlayerParams params) {
        Integer id = idGenerator.getAndIncrement();
        String category = params.age < 25 ? "JUNIOR" : "SENIOR";
        PlayerResponse player = new PlayerResponse(id, params.name, params.age, category, LocalDate.now());
        players.put(id, player);
        return player;
    }


    public PlayerResponse getPlayerById(int id) {
        return players.get(id);
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

