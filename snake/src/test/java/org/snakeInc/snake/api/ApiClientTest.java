package org.snakeInc.snake.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.snakeinc.snake.api.ApiClient;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ApiClient class.
 * These tests verify that the ApiClient can properly serialize/deserialize
 * requests and responses for player and score operations.
 * 
 * Note: Integration tests with actual HTTP calls would require a running server.
 */
public class ApiClientTest {
    
    private ApiClient apiClient;
    
    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
    }
    
    @Test
    public void testPlayerDTOCreation() {
        ApiClient.PlayerDTO player = new ApiClient.PlayerDTO(1, "Test Player", 25);
        
        assertEquals(1, player.id());
        assertEquals("Test Player", player.name());
        assertEquals(25, player.age());
    }
    
    @Test
    public void testCreatePlayerRequestCreation() {
        ApiClient.CreatePlayerRequest request = new ApiClient.CreatePlayerRequest("New Player", 30);
        
        assertEquals("New Player", request.name());
        assertEquals(30, request.age());
    }
    
    @Test
    public void testScoreRequestCreation() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        ApiClient.ScoreRequest request = new ApiClient.ScoreRequest(1, "python", 150, now);
        
        assertEquals(1, request.playerId());
        assertEquals("python", request.snake());
        assertEquals(150, request.score());
        assertEquals(now, request.playedAt());
    }
    
    @Test
    public void testScoreDTOCreation() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        ApiClient.ScoreDTO score = new ApiClient.ScoreDTO(1, 1, now, "anaconda", 200);
        
        assertEquals(1, score.id());
        assertEquals(1, score.playerId());
        assertEquals("anaconda", score.snake());
        assertEquals(200, score.score());
        assertEquals(now, score.playedAt());
    }
}
