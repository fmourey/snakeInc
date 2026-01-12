package com.snakeinc.api.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.snakeinc.api.ApiApplication;
import com.snakeinc.api.service.PlayerService.PlayerParams;
import com.snakeinc.api.service.PlayerService.PlayerResponse;
import com.snakeinc.api.service.ScoreService.ScoreParams;
import com.snakeinc.api.service.ScoreService.ScoreResponse;
import com.snakeinc.api.service.ScoreService.ScoresListResponse;
import com.snakeinc.api.service.ScoreService.ScoresStatsResponse;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;


@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ScoreControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private int createTestPlayer(String name, int age) {
        PlayerParams playerParams = new PlayerParams();
        playerParams.name = name;
        playerParams.age = age;

        ResponseEntity<PlayerResponse> response = restTemplate.postForEntity(
            "/api/v1/players",
            playerParams,
            PlayerResponse.class
        );

        return response.getBody().id();
    }

    @Test
    public void testCreateScoreSuccess() {
        int playerId = createTestPlayer("TestPlayer1", 30);

        ScoreParams params = new ScoreParams();
        params.playerId = playerId;
        params.snake = "python";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> response = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            ScoreResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playerId, response.getBody().playerId());
        assertEquals("python", response.getBody().snake());
        assertEquals(150, response.getBody().score());
    }

    @Test
    public void testCreateScoreValidationFails_InvalidSnakeName() {
        int playerId = createTestPlayer("TestPlayer2", 25);

        ScoreParams params = new ScoreParams();
        params.playerId = playerId;
        params.snake = "cobra";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateScoreValidationFails_NegativeScore() {
        int playerId = createTestPlayer("TestPlayer3", 28);

        ScoreParams params = new ScoreParams();
        params.playerId = playerId;
        params.snake = "python";
        params.score = -50;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Score cannot be negative") || 
                   response.getBody().contains("cannot be negative"));
    }

    @Test
    public void testCreateScoreValidationFails_PlayerNotFound() {
        ScoreParams params = new ScoreParams();
        params.playerId = 999;
        params.snake = "python";
        params.score = 150;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<?> response = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateScoreWithDifferentSnakes() {
        int playerId = createTestPlayer("TestPlayer4", 35);

        ScoreParams pythonParams = new ScoreParams();
        pythonParams.playerId = playerId;
        pythonParams.snake = "python";
        pythonParams.score = 100;
        pythonParams.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> pythonResponse = restTemplate.postForEntity(
            "/api/v1/scores",
            pythonParams,
            ScoreResponse.class
        );
        assertEquals("python", pythonResponse.getBody().snake());

        ScoreParams anacondaParams = new ScoreParams();
        anacondaParams.playerId = playerId;
        anacondaParams.snake = "anaconda";
        anacondaParams.score = 200;
        anacondaParams.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> anacondaResponse = restTemplate.postForEntity(
            "/api/v1/scores",
            anacondaParams,
            ScoreResponse.class
        );
        assertEquals("anaconda", anacondaResponse.getBody().snake());

        ScoreParams boaParams = new ScoreParams();
        boaParams.playerId = playerId;
        boaParams.snake = "boaConstrictor";
        boaParams.score = 300;
        boaParams.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> boaResponse = restTemplate.postForEntity(
            "/api/v1/scores",
            boaParams,
            ScoreResponse.class
        );
        assertEquals("boaConstrictor", boaResponse.getBody().snake());
    }

    @Test
    public void testGetScoreById() {
        int playerId = createTestPlayer("TestPlayer5", 32);

        ScoreParams params = new ScoreParams();
        params.playerId = playerId;
        params.snake = "python";
        params.score = 175;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            ScoreResponse.class
        );

        int scoreId = createResponse.getBody().id();

        ResponseEntity<ScoreResponse> getResponse = restTemplate.getForEntity(
            "/api/v1/scores/" + scoreId,
            ScoreResponse.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(scoreId, getResponse.getBody().id());
        assertEquals(playerId, getResponse.getBody().playerId());
        assertEquals("python", getResponse.getBody().snake());
        assertEquals(175, getResponse.getBody().score());
    }

    @Test
    public void testGetScoreByIdNotFound() {
        ResponseEntity<?> response = restTemplate.getForEntity(
            "/api/v1/scores/9999",
            Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testSearchScoresNoFilters() {
        int playerId1 = createTestPlayer("TestPlayer6", 27);
        int playerId2 = createTestPlayer("TestPlayer7", 31);

        ScoreParams params1 = new ScoreParams();
        params1.playerId = playerId1;
        params1.snake = "python";
        params1.score = 100;
        params1.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", params1, ScoreResponse.class);

        ScoreParams params2 = new ScoreParams();
        params2.playerId = playerId2;
        params2.snake = "anaconda";
        params2.score = 200;
        params2.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", params2, ScoreResponse.class);

        ResponseEntity<ScoresListResponse> response = restTemplate.getForEntity(
            "/api/v1/scores",
            ScoresListResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().scores().size() >= 2);
    }

    @Test
    public void testSearchScoresBySnake() {
        int playerId = createTestPlayer("TestPlayer8", 29);

        ScoreParams pythonParams = new ScoreParams();
        pythonParams.playerId = playerId;
        pythonParams.snake = "python";
        pythonParams.score = 100;
        pythonParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams, ScoreResponse.class);

        ScoreParams anacondaParams = new ScoreParams();
        anacondaParams.playerId = playerId;
        anacondaParams.snake = "anaconda";
        anacondaParams.score = 200;
        anacondaParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", anacondaParams, ScoreResponse.class);

        ResponseEntity<ScoresListResponse> response = restTemplate.getForEntity(
            "/api/v1/scores?snake=python",
            ScoresListResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().scores().stream().allMatch(s -> s.snake().equals("python")));
    }

    @Test
    public void testSearchScoresByPlayer() {
        int playerId1 = createTestPlayer("TestPlayer9", 26);
        int playerId2 = createTestPlayer("TestPlayer10", 34);

        ScoreParams params1 = new ScoreParams();
        params1.playerId = playerId1;
        params1.snake = "python";
        params1.score = 100;
        params1.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", params1, ScoreResponse.class);

        ScoreParams params2 = new ScoreParams();
        params2.playerId = playerId2;
        params2.snake = "anaconda";
        params2.score = 200;
        params2.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", params2, ScoreResponse.class);

        ResponseEntity<ScoresListResponse> response = restTemplate.getForEntity(
            "/api/v1/scores?player=" + playerId1,
            ScoresListResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().scores().stream().allMatch(s -> s.playerId() == playerId1));
    }

    @Test
    public void testSearchScoresBySnakeAndPlayer() {
        int playerId = createTestPlayer("TestPlayer11", 33);

        ScoreParams pythonParams = new ScoreParams();
        pythonParams.playerId = playerId;
        pythonParams.snake = "python";
        pythonParams.score = 100;
        pythonParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams, ScoreResponse.class);

        ScoreParams anacondaParams = new ScoreParams();
        anacondaParams.playerId = playerId;
        anacondaParams.snake = "anaconda";
        anacondaParams.score = 200;
        anacondaParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", anacondaParams, ScoreResponse.class);

        ResponseEntity<ScoresListResponse> response = restTemplate.getForEntity(
            "/api/v1/scores?snake=python&player=" + playerId,
            ScoresListResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().scores().size() >= 1);
        assertTrue(response.getBody().scores().stream()
            .allMatch(s -> s.snake().equals("python") && s.playerId() == playerId));
    }

    @Test
    public void testGetStatsForPlayer() {
        int playerId = createTestPlayer("TestPlayer12", 30);

        ScoreParams pythonParams1 = new ScoreParams();
        pythonParams1.playerId = playerId;
        pythonParams1.snake = "python";
        pythonParams1.score = 100;
        pythonParams1.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams1, ScoreResponse.class);

        ScoreParams pythonParams2 = new ScoreParams();
        pythonParams2.playerId = playerId;
        pythonParams2.snake = "python";
        pythonParams2.score = 200;
        pythonParams2.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams2, ScoreResponse.class);

        ScoreParams pythonParams3 = new ScoreParams();
        pythonParams3.playerId = playerId;
        pythonParams3.snake = "python";
        pythonParams3.score = 300;
        pythonParams3.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams3, ScoreResponse.class);

        ScoreParams anacondaParams = new ScoreParams();
        anacondaParams.playerId = playerId;
        anacondaParams.snake = "anaconda";
        anacondaParams.score = 150;
        anacondaParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", anacondaParams, ScoreResponse.class);

        ResponseEntity<ScoresStatsResponse> response = restTemplate.getForEntity(
            "/api/v1/scores/stats?player=" + playerId,
            ScoresStatsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playerId, response.getBody().playerId());
        assertEquals(300, response.getBody().bestScore());
        assertEquals(4, response.getBody().totalGames());
    }

    @Test
    public void testGetStatsForPlayerNotFound() {
        ResponseEntity<?> response = restTemplate.getForEntity(
            "/api/v1/scores/stats?player=9999",
            Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetStatsMultipleSnakeTypes() {
        int playerId = createTestPlayer("TestPlayer13", 28);

        ScoreParams pythonParams = new ScoreParams();
        pythonParams.playerId = playerId;
        pythonParams.snake = "python";
        pythonParams.score = 120;
        pythonParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", pythonParams, ScoreResponse.class);

        ScoreParams anacondaParams = new ScoreParams();
        anacondaParams.playerId = playerId;
        anacondaParams.snake = "anaconda";
        anacondaParams.score = 180;
        anacondaParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", anacondaParams, ScoreResponse.class);

        ScoreParams boaParams = new ScoreParams();
        boaParams.playerId = playerId;
        boaParams.snake = "boaConstrictor";
        boaParams.score = 220;
        boaParams.playedAt = LocalDateTime.now();
        restTemplate.postForEntity("/api/v1/scores", boaParams, ScoreResponse.class);

        ResponseEntity<ScoresStatsResponse> response = restTemplate.getForEntity(
            "/api/v1/scores/stats?player=" + playerId,
            ScoresStatsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playerId, response.getBody().playerId());
        assertEquals(3, response.getBody().totalGames());
    }

    @Test
    public void testGameScoreSubmission() {
        int playerId = createTestPlayer("GamePlayer", 27);

        ScoreParams params = new ScoreParams();
        params.playerId = playerId;
        params.snake = "anaconda";
        params.score = 250;
        params.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> response = restTemplate.postForEntity(
            "/api/v1/scores",
            params,
            ScoreResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playerId, response.getBody().playerId());
        assertEquals("anaconda", response.getBody().snake());
        assertEquals(250, response.getBody().score());
    }

    @Test
    public void testMultipleGameScoresForSamePlayer() {
        int playerId = createTestPlayer("MultiGamePlayer", 30);

        ScoreParams params1 = new ScoreParams();
        params1.playerId = playerId;
        params1.snake = "anaconda";
        params1.score = 100;
        params1.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> response1 = restTemplate.postForEntity(
            "/api/v1/scores",
            params1,
            ScoreResponse.class
        );
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ScoreParams params2 = new ScoreParams();
        params2.playerId = playerId;
        params2.snake = "python";
        params2.score = 200;
        params2.playedAt = LocalDateTime.now();

        ResponseEntity<ScoreResponse> response2 = restTemplate.postForEntity(
            "/api/v1/scores",
            params2,
            ScoreResponse.class
        );
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        ResponseEntity<ScoresListResponse> listResponse = restTemplate.getForEntity(
            "/api/v1/scores?player=" + playerId,
            ScoresListResponse.class
        );

        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertEquals(2, listResponse.getBody().scores().size());
    }
}
