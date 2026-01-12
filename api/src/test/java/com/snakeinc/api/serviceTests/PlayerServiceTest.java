package com.snakeinc.api.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.snakeinc.api.model.Player;
import com.snakeinc.api.repository.PlayerRepository;
import com.snakeinc.api.service.PlayerService;
import com.snakeinc.api.service.PlayerService.PlayerParams;
import com.snakeinc.api.service.PlayerService.PlayerResponse;
import java.time.LocalDate;
import java.util.Optional;

public class PlayerServiceTest {
    
    private PlayerService playerService;
    
    @Mock
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(playerRepository);
    }

    @Test
    public void testCreatePlayerSuccess() {
        PlayerParams params = new PlayerParams();
        params.name = "John Doe";
        params.age = 30;

        Player mockPlayer = new Player("John Doe", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        PlayerResponse response = playerService.createPlayer(params);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals(30, response.age());
        assertEquals("SENIOR", response.category());
        assertNotNull(response.createdAt());
        assertEquals(1, response.id());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void testCreatePlayerJunior() {
        PlayerParams params = new PlayerParams();
        params.name = "Alice";
        params.age = 20;

        Player mockPlayer = new Player("Alice", 20, "JUNIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        PlayerResponse response = playerService.createPlayer(params);

        assertEquals("JUNIOR", response.category());
        verify(playerRepository, times(1)).save(any(Player.class));
    }


    @Test
    public void testGetPlayerById() {
        Player mockPlayer = new Player("Charlie", 35, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        PlayerResponse retrieved = playerService.getPlayerById(1);

        assertNotNull(retrieved);
        assertEquals("Charlie", retrieved.name());
        assertEquals(35, retrieved.age());
        assertEquals(1, retrieved.id());
        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    public void testGetPlayerByIdNotFound() {
        when(playerRepository.findById(999)).thenReturn(Optional.empty());
        
        PlayerResponse result = playerService.getPlayerById(999);
        assertNull(result);
        verify(playerRepository, times(1)).findById(999);
    }
}


