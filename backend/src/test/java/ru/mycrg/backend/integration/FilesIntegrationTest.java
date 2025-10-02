package ru.mycrg.backend.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mycrg.backend.dto.FilesDto;

import java.util.List;

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
        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().size() == 0);
    }

    @Test
    public void successfullyAddFile() {
        //Scenario: Добавление файла, с правильным токеном проходит успешно
        //Given я авторизован как "admin"
        //When  я делаю запрос на добавления файла
        //Then  сервер отвечает со статусом: 200
        //And   в ответе есть "size" и "filename"
        String authToken = extractTokenFromResponse(loginAsAdmin());
        String filename = "smallTestFileOne.txt";

        ResponseEntity<FilesDto> response = addFileOnCloud(authToken, filename);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().getFilename().equals(filename));
    }

    private ResponseEntity<List<FilesDto>> getFilesList(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "Bearer " + authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Выполняем запрос
        return restTemplate.exchange(
                "http://10.10.10.61:8084/list?limit=3",
                GET,
                entity,
                List<FilesDto>.class
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

    private ResponseEntity<FilesDto> addFileOnCloud(String authToken, String filename) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_TOKEN_HEADER, "Bearer " + authToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ClassPathResource fileResource = new ClassPathResource(filename);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                "http://10.10.10.61:8084/file?filename=" + filename,
                POST,
                entity,
                FilesDto.class
        );
    }
}
