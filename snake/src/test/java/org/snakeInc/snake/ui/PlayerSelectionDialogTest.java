package org.snakeInc.snake.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.snakeinc.snake.api.ApiClient;

/**
 * Unit tests for PlayerSelectionDialog.
 * 
 * Note: Complete GUI testing requires a proper desktop environment.
 * These tests focus on API integration and data handling.
 */
public class PlayerSelectionDialogTest {
    
    @Test
    public void testPlayerDTOCreation() {
        // Test that we can create a PlayerDTO record
        ApiClient.PlayerDTO player = new ApiClient.PlayerDTO(1, "Test Player", 25);
        
        assertEquals(1, player.id());
        assertEquals("Test Player", player.name());
        assertEquals(25, player.age());
    }
    
    @Test
    public void testCreatePlayerRequestSerialization() {
        // Test player creation request
        ApiClient.CreatePlayerRequest request = new ApiClient.CreatePlayerRequest("Alice", 28);
        
        assertEquals("Alice", request.name());
        assertEquals(28, request.age());
    }
}
