package ru.mycrg.backend.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mycrg.backend.dto.request.LoginDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.POST;
import static ru.mycrg.backend.integration.FilesIntegrationTest.getFilesListWithBadToken;

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
    void authWithUnCorrectCredentials() {
        //Scenario: Авторизация с НЕ валидными учётными данными вызовет 400
        //When  я авторизуюсь как "Несуществующий пользователь"
        //Then  сервер отвечает со статусом: 400
        //And   в ответе содержится "Bad credentials"

        ResponseEntity<String> response = loginAsWithErrorHandling("TeaPot");

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody() != null && response.getBody().contains("Bad credentials"));
    }

    @Test
    void successfullyLogOut() {
        //Scenario: Успешный выход с валидным токеном
        //Given я авторизован как "admin"
        //When  я делаю logout
        //Then  запросы с тем же токеном возвращают статус 401
        String authToken = extractTokenFromResponse(loginAsAdmin());

        logout(authToken);

        ResponseEntity<String> response = getFilesListWithBadToken(authToken);
        assertSame(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    public static ResponseEntity<String> loginAsAdmin() {
        return loginAs("admin");
    }

    public static ResponseEntity<String> loginAs(String user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginDto requestBody = new LoginDto(user, user);

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

    public static ResponseEntity<String> loginAsWithErrorHandling(String user) {
        try {
            return loginAs(user);
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                                 .headers(ex.getResponseHeaders())
                                 .body(ex.getResponseBodyAsString());
        }
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
