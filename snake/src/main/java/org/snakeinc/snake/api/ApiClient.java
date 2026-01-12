package org.snakeinc.snake.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public PlayerDTO createPlayer(String name, int age) throws IOException, InterruptedException {
        CreatePlayerRequest request = new CreatePlayerRequest(name, age);
        String json = objectMapper.writeValueAsString(request);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/players"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create player: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), PlayerDTO.class);
    }
    
    public PlayerDTO getPlayerByName(String name) throws IOException, InterruptedException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/players?name=" + encodedName))
            .header("Content-Type", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load player: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), PlayerDTO.class);
    }
    
    public ScoreDTO submitScore(int playerId, String snakeName, int score) throws IOException, InterruptedException {
        ScoreRequest scoreRequest = new ScoreRequest(playerId, snakeName, score, LocalDateTime.now());
        String json = objectMapper.writeValueAsString(scoreRequest);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/scores"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to submit score: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), ScoreDTO.class);
    }
    
    public StatsDTO getPlayerStats(int playerId) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/scores/stats?player=" + playerId))
            .header("Content-Type", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get player stats: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), StatsDTO.class);
    }
    
    public LeaderboardDTO getTopScores(int limit) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/scores?limit=" + limit))
            .header("Content-Type", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get leaderboard: " + response.body());
        }
        
        return objectMapper.readValue(response.body(), LeaderboardDTO.class);
    }
    
    // DTOs
    public record CreatePlayerRequest(String name, int age) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PlayerDTO(int id, String name, int age) {}
    
    public record ScoreRequest(int playerId, String snake, int score, LocalDateTime playedAt) {}
    
    public record ScoreDTO(int id, int playerId, LocalDateTime playedAt, String snake, int score) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StatsDTO(int playerId, int totalGames, int bestScore, double averageScore) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LeaderboardDTO(java.util.List<LeaderboardEntryDTO> scores) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LeaderboardEntryDTO(int score, String playerName, String snake) {}
}
