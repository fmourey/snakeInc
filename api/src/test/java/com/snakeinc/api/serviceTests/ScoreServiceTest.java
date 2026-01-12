package com.snakeinc.api.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.snakeinc.api.model.Player;
import com.snakeinc.api.model.Score;
import com.snakeinc.api.repository.PlayerRepository;
import com.snakeinc.api.repository.ScoreRepository;
import com.snakeinc.api.service.ScoreService;
import com.snakeinc.api.service.ScoreService.ScoreParams;
import com.snakeinc.api.service.ScoreService.ScoreResponse;
import com.snakeinc.api.service.ScoreService.ScoresListResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreServiceTest {
    
    private ScoreService scoreService;
    
    @Mock
    private ScoreRepository scoreRepository;
    
    @Mock
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        scoreService = new ScoreService(scoreRepository, playerRepository);
    }

    @Test
    public void testCreateScoreSuccess() {
        ScoreParams params = new ScoreParams();
        params.playerId = 1;
        params.snake = "python";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        Score mockScore = new Score(mockPlayer, params.playedAt, "python", 150);
        mockScore.setId(1);
        when(scoreRepository.save(any(Score.class))).thenReturn(mockScore);

        ScoreResponse response = scoreService.createScore(params);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.playerId());
        assertEquals("python", response.snake());
        assertEquals(150, response.score());
        verify(playerRepository, times(1)).findById(1);
        verify(scoreRepository, times(1)).save(any(Score.class));
    }

    @Test
    public void testCreateScorePlayerNotFound() {
        ScoreParams params = new ScoreParams();
        params.playerId = 999;
        params.snake = "python";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        when(playerRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            scoreService.createScore(params);
        }, "Player with id 999 does not exist");
        
        verify(playerRepository, times(1)).findById(999);
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    public void testCreateScoreInvalidSnakeName() {
        ScoreParams params = new ScoreParams();
        params.playerId = 1;
        params.snake = "cobra";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        assertThrows(IllegalArgumentException.class, () -> {
            scoreService.createScore(params);
        }, "Invalid snake name");
        
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    public void testCreateScoreNegativeScore() {
        ScoreParams params = new ScoreParams();
        params.playerId = 1;
        params.snake = "python";
        params.score = -10;
        params.playedAt = LocalDateTime.now();

        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        assertThrows(IllegalArgumentException.class, () -> {
            scoreService.createScore(params);
        }, "Score cannot be negative");
        
        verify(scoreRepository, never()).save(any(Score.class));
    }

    @Test
    public void testCreateScoreWithDifferentSnakes() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        ScoreParams paramsAnaconda = new ScoreParams();
        paramsAnaconda.playerId = 1;
        paramsAnaconda.snake = "anaconda";
        paramsAnaconda.score = 200;
        paramsAnaconda.playedAt = LocalDateTime.now();

        Score mockScore1 = new Score(mockPlayer, paramsAnaconda.playedAt, "anaconda", 200);
        mockScore1.setId(1);
        when(scoreRepository.save(any(Score.class))).thenReturn(mockScore1);

        ScoreResponse response1 = scoreService.createScore(paramsAnaconda);
        assertEquals("anaconda", response1.snake());

        ScoreParams paramsBoaConstrictor = new ScoreParams();
        paramsBoaConstrictor.playerId = 1;
        paramsBoaConstrictor.snake = "boaConstrictor";
        paramsBoaConstrictor.score = 300;
        paramsBoaConstrictor.playedAt = LocalDateTime.now();

        Score mockScore2 = new Score(mockPlayer, paramsBoaConstrictor.playedAt, "boaConstrictor", 300);
        mockScore2.setId(2);
        when(scoreRepository.save(any(Score.class))).thenReturn(mockScore2);

        ScoreResponse response2 = scoreService.createScore(paramsBoaConstrictor);
        assertEquals("boaConstrictor", response2.snake());
    }

    @Test
    public void testGetScoreById() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        
        Score mockScore = new Score(mockPlayer, LocalDateTime.now(), "python", 150);
        mockScore.setId(1);
        when(scoreRepository.findById(1)).thenReturn(Optional.of(mockScore));

        ScoreResponse response = scoreService.getScoreById(1);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals(1, response.playerId());
        assertEquals("python", response.snake());
        assertEquals(150, response.score());
        verify(scoreRepository, times(1)).findById(1);
    }

    @Test
    public void testGetScoreByIdNotFound() {
        when(scoreRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            scoreService.getScoreById(999);
        }, "Score with id 999 not found");
        
        verify(scoreRepository, times(1)).findById(999);
    }

    @Test
    public void testSearchScoresNoFilters() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        
        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 100));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "anaconda", 200));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoresListResponse response = scoreService.searchScores(Optional.empty(), Optional.empty());

        assertNotNull(response);
        assertEquals(2, response.scores().size());
        verify(scoreRepository, times(1)).findAll();
    }

    @Test
    public void testSearchScoresBySnake() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        
        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 100));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "anaconda", 200));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 150));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoresListResponse response = scoreService.searchScores(Optional.of("python"), Optional.empty());

        assertNotNull(response);
        assertEquals(2, response.scores().size());
        assertTrue(response.scores().stream().allMatch(s -> s.snake().equals("python")));
    }

    @Test
    public void testSearchScoresByPlayer() {
        Player mockPlayer1 = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer1.setId(1);
        Player mockPlayer2 = new Player("Jane", 25, "JUNIOR", LocalDate.now());
        mockPlayer2.setId(2);
        
        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer1, LocalDateTime.now(), "python", 100));
        mockScores.add(new Score(mockPlayer2, LocalDateTime.now(), "anaconda", 200));
        mockScores.add(new Score(mockPlayer1, LocalDateTime.now(), "python", 150));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoresListResponse response = scoreService.searchScores(Optional.empty(), Optional.of(1));

        assertNotNull(response);
        assertEquals(2, response.scores().size());
        assertTrue(response.scores().stream().allMatch(s -> s.playerId() == 1));
    }

    @Test
    public void testSearchScoresBySnakeAndPlayer() {
        Player mockPlayer1 = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer1.setId(1);
        Player mockPlayer2 = new Player("Jane", 25, "JUNIOR", LocalDate.now());
        mockPlayer2.setId(2);
        
        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer1, LocalDateTime.now(), "python", 100));
        mockScores.add(new Score(mockPlayer2, LocalDateTime.now(), "python", 200));
        mockScores.add(new Score(mockPlayer1, LocalDateTime.now(), "anaconda", 150));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoresListResponse response = scoreService.searchScores(Optional.of("python"), Optional.of(1));

        assertNotNull(response);
        assertEquals(1, response.scores().size());
        assertEquals("python", response.scores().get(0).snake());
        assertEquals(1, response.scores().get(0).playerId());
    }

    @Test
    public void testGetStatsForPlayer() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 100));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 200));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 300));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "anaconda", 150));
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "anaconda", 250));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoreService.ScoresStatsResponse response = scoreService.getStatsForPlayer(1);

        assertNotNull(response);
        assertEquals(1, response.playerId());
        assertEquals(5, response.totalGames());
        assertEquals(300, response.bestScore());
        assertEquals(200.0, response.averageScore(), 0.1);
    }

    @Test
    public void testGetStatsForPlayerNotFound() {
        when(playerRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            scoreService.getStatsForPlayer(999);
        }, "Player with id 999 does not exist");
        
        verify(playerRepository, times(1)).findById(999);
    }

    @Test
    public void testGetStatsForPlayerWithNoScores() {
        Player mockPlayer = new Player("John", 30, "SENIOR", LocalDate.now());
        mockPlayer.setId(1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(mockPlayer));

        List<Score> mockScores = new ArrayList<>();
        mockScores.add(new Score(mockPlayer, LocalDateTime.now(), "python", 100));
        Player otherPlayer = new Player("Jane", 25, "JUNIOR", LocalDate.now());
        otherPlayer.setId(2);
        mockScores.add(new Score(otherPlayer, LocalDateTime.now(), "python", 200));
        
        when(scoreRepository.findAll()).thenReturn(mockScores);

        ScoreService.ScoresStatsResponse response = scoreService.getStatsForPlayer(1);

        assertNotNull(response);
        assertEquals(1, response.playerId());
        assertEquals(1, response.totalGames());
    }
}
