package com.snakeinc.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snakeinc.api.service.PlayerService;
import com.snakeinc.api.service.PlayerService.PlayerParams;
import com.snakeinc.api.service.PlayerService.PlayerResponse;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public PlayerResponse createPlayer(@Valid @RequestBody PlayerParams params) {
        return playerService.createPlayer(params);
    }

    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable int id) {
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public PlayerResponse getPlayerByName(@RequestParam(name = "name") String name) {
        return playerService.getPlayerByName(name);
    }
}


