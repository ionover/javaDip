package ru.mycrg.backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mycrg.backend.dto.LoginDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class AuthIntegrationTest {

    public static final String AUTH_TOKEN_HEADER = "auth-token";

    @Test
    void successfullyAuthWithCorrectCredentials() {
        ResponseEntity<String> response = loginAsAdmin();

        // Проверяем результат
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().contains(AUTH_TOKEN_HEADER));
    }

    public static ResponseEntity<String> loginAsAdmin() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginDto requestBody = new LoginDto("admin", "admin");

        HttpEntity<LoginDto> entity = new HttpEntity<>(requestBody, headers);

        // Выполняем запрос
        return restTemplate.postForEntity(
                "http://10.10.10.61:8084/login",
                entity,
                String.class
        );
    }
}
