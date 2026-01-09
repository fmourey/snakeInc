package com.snakeinc.api.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import com.snakeinc.api.service.PlayerService;
import com.snakeinc.api.service.PlayerService.PlayerParams;
import com.snakeinc.api.service.PlayerService.PlayerResponse;

public class PlayerServiceTest {
    
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerService = new PlayerService();
    }

    @Test
    public void testCreatePlayerSuccess() {
        PlayerParams params = new PlayerParams();
        params.name = "John Doe";
        params.age = 30;

        PlayerResponse response = playerService.createPlayer(params);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals(30, response.age());
        assertEquals("SENIOR", response.category());
        assertNotNull(response.createdAt());
        assertEquals(1, response.id());
    }

    @Test
    public void testCreatePlayerJunior() {
        PlayerParams params = new PlayerParams();
        params.name = "Alice";
        params.age = 20;

        PlayerResponse response = playerService.createPlayer(params);

        assertEquals("JUNIOR", response.category());
    }


    @Test
    public void testGetPlayerById() {
        PlayerParams params = new PlayerParams();
        params.name = "Charlie";
        params.age = 35;

        PlayerResponse created = playerService.createPlayer(params);
        PlayerResponse retrieved = playerService.getPlayerById(created.id());

        assertNotNull(retrieved);
        assertEquals(created.name(), retrieved.name());
        assertEquals(created.age(), retrieved.age());
        assertEquals(created.id(), retrieved.id());
    }

    @Test
    public void testGetPlayerByIdNotFound() {
        PlayerResponse result = playerService.getPlayerById(999);
        assertNull(result);
    }

    @Test
    public void testMultiplePlayersWithIncrementingIds() {
        PlayerParams params1 = new PlayerParams();
        params1.name = "Player1";
        params1.age = 20;

        PlayerParams params2 = new PlayerParams();
        params2.name = "Player2";
        params2.age = 30;

        PlayerResponse response1 = playerService.createPlayer(params1);
        PlayerResponse response2 = playerService.createPlayer(params2);

        assertEquals(1, response1.id());
        assertEquals(2, response2.id());
    }
}

