package ru.mycrg.backend.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mycrg.backend.dto.FilesDto;
import ru.mycrg.backend.dto.LoginDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.POST;
import static ru.mycrg.backend.integration.FilesIntegrationTest.getFilesList;

@Testcontainers
public class AuthIntegrationTest {

    public static final String AUTH_TOKEN_HEADER = "auth-token";

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Test
    void successfullyAuthWithCorrectCredentials() {
        //Scenario: Успешная авторизация с валидными учётными данными
        //When  я авторизуюсь как "admin"
        //Then  сервер отвечает со статусом: 200
        //And   в ответе содержится ожидаемый ответ
        ResponseEntity<String> response = loginAsAdmin();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().contains(AUTH_TOKEN_HEADER));
    }

    @Test
    void successfullyLogOut() {
        //Scenario: Успешный выход с валидным токеном
        //Given я авторизован как "admin"
        //When  я делаю logout
        //Then  сервер отвечает со статусом: 200
        //And   запросы с тем же токеном возвращают статус 400
        String authToken = extractTokenFromResponse(loginAsAdmin());

        logout(authToken);

        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    public static ResponseEntity<String> loginAsAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginDto requestBody = new LoginDto("admin", "admin");

        HttpEntity<LoginDto> entity = new HttpEntity<>(requestBody, headers);

        return REST_TEMPLATE.postForEntity(
                "http://10.10.10.61:8084/login",
                entity,
                String.class
        );
    }

    private void logout(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_TOKEN_HEADER, "Bearer " + authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        REST_TEMPLATE.exchange(
                "http://10.10.10.61:8084/logout",
                POST,
                entity,
                Void.class
        );
    }

    public static String extractTokenFromResponse(ResponseEntity<String> stringResponseEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(stringResponseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonNode.get(AUTH_TOKEN_HEADER).asText();
    }
}
