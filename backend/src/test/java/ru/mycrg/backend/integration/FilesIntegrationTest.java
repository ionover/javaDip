package ru.mycrg.backend.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mycrg.backend.integration.AuthIntegrationTest.AUTH_TOKEN_HEADER;

@Testcontainers
public class FilesIntegrationTest {

    private final AuthIntegrationTest authIntegrationTest;

    public FilesIntegrationTest(AuthIntegrationTest authIntegrationTest) {
        this.authIntegrationTest = authIntegrationTest;
    }

    @Test
    void emptyListWhenNoFilesAdd() {
        String token = extractTokenFromResponse(authIntegrationTest.loginAsAdmin());
        ResponseEntity<String> response = getFilesList(token);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

//    @Test
//    public void successfullyAddFile() {
//    }

    private ResponseEntity<String> getFilesList(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "Bearer " + authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Выполняем запрос
        return restTemplate.exchange(
                "http://10.10.10.61:8084/list?limit=3",
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );
    }

    private String extractTokenFromResponse(ResponseEntity<String> stringResponseEntity) {
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
