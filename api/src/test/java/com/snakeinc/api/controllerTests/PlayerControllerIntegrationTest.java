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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreatePlayerSuccess() {
        PlayerParams params = new PlayerParams();
        params.name = "John Doe";
        params.age = 30;

        ResponseEntity<PlayerResponse> response = restTemplate.postForEntity(
            "/api/v1/players",
            params,
            PlayerResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().name());
        assertEquals(30, response.getBody().age());
        assertEquals("SENIOR", response.getBody().category());
    }

    @Test
    public void testCreatePlayerValidationFails_NullName() {
        PlayerParams params = new PlayerParams();
        params.name = null;
        params.age = 30;

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/v1/players",
            params,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Name cannot be null"));
    }

    @Test
    public void testCreatePlayerValidationFails_InvalidAge() {
        PlayerParams params = new PlayerParams();
        params.name = "Jane Doe";
        params.age = 10;

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/v1/players",
            params,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Age must be greater than 13"));
    }

    @Test
    public void testGetPlayerById() {
        PlayerParams params = new PlayerParams();
        params.name = "Alice";
        params.age = 25;

        ResponseEntity<PlayerResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/players",
            params,
            PlayerResponse.class
        );

        PlayerResponse created = createResponse.getBody();
        int playerId = created.id();

        ResponseEntity<PlayerResponse> getResponse = restTemplate.getForEntity(
            "/api/v1/players/" + playerId,
            PlayerResponse.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Alice", getResponse.getBody().name());
        assertEquals(25, getResponse.getBody().age());
    }

    @Test
    public void testGetPlayerByIdNotFound() {
        ResponseEntity<PlayerResponse> response = restTemplate.getForEntity(
            "/api/v1/players/999",
            PlayerResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

}
