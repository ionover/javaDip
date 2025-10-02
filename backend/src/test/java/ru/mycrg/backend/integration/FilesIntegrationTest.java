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
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static ru.mycrg.backend.integration.AuthIntegrationTest.AUTH_TOKEN_HEADER;
import static ru.mycrg.backend.integration.AuthIntegrationTest.loginAsAdmin;

@Testcontainers
public class FilesIntegrationTest {

    @Test
    void emptyListWhenNoFilesAdd() {
        //Scenario: Если в облаке нет никаких файлов, то получим пустой массив
        //Given я авторизован как "admin"
        //When  я делаю запрос на получения списка файлов
        //Then  сервер отвечает со статусом: 200
        //And   в ответе находится пустой массив
        String authToken = extractTokenFromResponse(loginAsAdmin());
        ResponseEntity<String> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().startsWith("[]"));
    }

    @Test
    public void successfullyAddFile() {
        //Scenario: Добавление файла, с правильным токеном проходит успешно
        //Given я авторизован как "admin"
        //When  я делаю запрос на добавления файла
        //Then  сервер отвечает со статусом: 200
        //And   в ответе есть "size" и "filename"
        String authToken = extractTokenFromResponse(loginAsAdmin());

        addFileOnCloud(authToken);

        ResponseEntity<String> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().contains("size"));
        assertTrue(response.getBody() != null && response.getBody().contains("filename"));
    }

    private ResponseEntity<String> getFilesList(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "Bearer " + authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Выполняем запрос
        return restTemplate.exchange(
                "http://10.10.10.61:8084/list?limit=3",
                GET,
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

    private void addFileOnCloud(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "Bearer " + authToken);

        //В этом тесте используем файл smallTestFileOne.txt

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Выполняем запрос
        restTemplate.exchange(
                "http://10.10.10.61:8084/file",
                POST,
                entity,
                String.class
        );
    }
}
