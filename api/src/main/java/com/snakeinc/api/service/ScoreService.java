package com.snakeinc.api.service;

import org.springframework.stereotype.Service;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.snakeinc.api.model.Player;
import com.snakeinc.api.model.Score;
import com.snakeinc.api.repository.PlayerRepository;
import com.snakeinc.api.repository.ScoreRepository;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;
    
    public ScoreService(ScoreRepository scoreRepository, PlayerRepository playerRepository) {
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
    }

    public ScoreResponse createScore(ScoreParams params) {
        Optional<Player> playerOpt = playerRepository.findById(params.playerId);
        if (playerOpt.isEmpty()) {
            throw new IllegalArgumentException("Player with id " + params.playerId + " does not exist");
        }
        
        if (!isValidSnakeName(params.snake)) {
            throw new IllegalArgumentException("Invalid snake name. Must be one of: python, anaconda, boaConstrictor");
        }
        
        if (params.score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        
        Player player = playerOpt.get();
        Score score = new Score(player, params.playedAt, params.snake, params.score);
        Score savedScore = scoreRepository.save(score);
        
        return new ScoreResponse(savedScore.getId(), savedScore.getPlayer().getId(), 
                                savedScore.getPlayedAt(), savedScore.getSnake(), savedScore.getScore());
    }

    public ScoreResponse getScoreById(int id) {
        Optional<Score> score = scoreRepository.findById(id);
        if (score.isPresent()) {
            Score s = score.get();
            return new ScoreResponse(s.getId(), s.getPlayer().getId(), s.getPlayedAt(), s.getSnake(), s.getScore());
        }
        throw new IllegalArgumentException("Score with id " + id + " not found");
    }

    public ScoresListResponse searchScores(Optional<String> snake, Optional<Integer> playerId) {
        Iterable<Score> allScores = scoreRepository.findAll();
        
        List<ScoreListItem> filteredScores = ((List<Score>) allScores).stream()
            .filter(score -> snake.isEmpty() || score.getSnake().equals(snake.get()))
            .filter(score -> playerId.isEmpty() || score.getPlayer().getId().equals(playerId.get()))
            .map(score -> new ScoreListItem(
                score.getSnake(),
                score.getScore(),
                score.getPlayedAt().toLocalDate(),
                score.getPlayer().getId()
            ))
            .collect(Collectors.toList());
        
        return new ScoresListResponse(filteredScores);
    }

    public ScoresStatsResponse getStatsForPlayer(int playerId) {
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            throw new IllegalArgumentException("Player with id " + playerId + " does not exist");
        }
        
        Iterable<Score> allScores = scoreRepository.findAll();
        List<Score> playerScores = ((List<Score>) allScores).stream()
            .filter(score -> score.getPlayer().getId().equals(playerId))
            .collect(Collectors.toList());
        
        int totalGames = playerScores.size();
        int bestScore = playerScores.stream().mapToInt(Score::getScore).max().orElse(0);
        double averageScore = playerScores.stream().mapToInt(Score::getScore).average().orElse(0);
        
        return new ScoresStatsResponse(playerId, totalGames, bestScore, averageScore);
    }

    public LeaderboardResponse getTopScores(int limit) {
        Iterable<Score> allScores = scoreRepository.findAll();
        List<LeaderboardItem> topScores = ((List<Score>) allScores).stream()
            .map(score -> new LeaderboardItem(
                score.getScore(),
                score.getPlayer().getName(),
                score.getSnake()
            ))
            .sorted((a, b) -> Integer.compare(b.score(), a.score()))
            .limit(limit)
            .collect(Collectors.toList());
        
        return new LeaderboardResponse(topScores);
    }

    private boolean isValidSnakeName(String snake) {
        return snake != null && (snake.equals("python") || snake.equals("anaconda") || snake.equals("boaConstrictor")); 
    }

    public static class ScoreParams {
        @NotNull(message = "PlayerId cannot be null")
        public Integer playerId;
        
        @NotNull(message = "Snake cannot be null")
        public String snake;
        
        @NotNull(message = "Score cannot be null")
        @Min(value = 0, message = "Score cannot be negative")
        public Integer score;
        
        @NotNull(message = "PlayedAt cannot be null")
        public LocalDateTime playedAt;
    }

    public record ScoreResponse(int id, int playerId, LocalDateTime playedAt, String snake, int score) {
    }

    public record ScoreListItem(String snake, int score, LocalDate playedAt, int playerId) {
    }

    public record ScoresListResponse(List<ScoreListItem> scores) {
    }

    public record SnakeStats(String snake, int min, int max, int average) {
    }

    public record ScoresStatsResponse(int playerId, int totalGames, int bestScore, double averageScore) {
    }
    
    public record LeaderboardItem(int score, String playerName, String snake) {
    }
    
    public record LeaderboardResponse(List<LeaderboardItem> scores) {
    }
}

