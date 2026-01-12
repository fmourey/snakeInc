package com.snakeinc.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snakeinc.api.service.ScoreService;
import com.snakeinc.api.service.ScoreService.ScoreParams;
import com.snakeinc.api.service.ScoreService.ScoreResponse;
import com.snakeinc.api.service.ScoreService.ScoresListResponse;
import com.snakeinc.api.service.ScoreService.ScoresStatsResponse;
import com.snakeinc.api.service.ScoreService.LeaderboardResponse;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping
    public ScoreResponse createScore(@Valid @RequestBody ScoreParams params) {
        return scoreService.createScore(params);
    }

    @GetMapping("/{id}")
    public ScoreResponse getScoreById(@PathVariable int id) {
        return scoreService.getScoreById(id);
    }

    @GetMapping("/stats")
    public ScoresStatsResponse getStatsForPlayer(@RequestParam(name = "player") int playerId) {
        return scoreService.getStatsForPlayer(playerId);
    }

    @GetMapping
    public Object searchScores(
        @RequestParam(name = "snake", required = false) Optional<String> snake,
        @RequestParam(name = "player", required = false) Optional<Integer> playerId,
        @RequestParam(name = "limit", required = false) Optional<Integer> limit) {
        
        if (limit.isPresent()) {
            return scoreService.getTopScores(limit.get());
        }
        
        return scoreService.searchScores(snake, playerId);
    }

}


