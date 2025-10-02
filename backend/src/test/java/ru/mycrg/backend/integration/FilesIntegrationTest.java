package ru.mycrg.backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
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
import ru.mycrg.backend.dto.UpdateFilenameDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;
import static ru.mycrg.backend.integration.AuthIntegrationTest.*;

@Testcontainers
public class FilesIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void emptyListWhenNoFilesAdd() {
        //Scenario: Если в облаке нет файлов, то с валидным токеном получим пустой массив
        //Given я авторизован как "admin"
        //*     на сервере нет файлов
        //When  я делаю запрос на получения списка файлов
        //Then  сервер отвечает со статусом: 200
        //And   в ответе находится пустой массив
        String authToken = extractTokenFromResponse(loginAsAdmin());

        deleteAllIfExist(authToken);

        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void successfullyAddFile() {
        //Scenario: Добавление файла с валидным токеном проходит успешно
        //Given я авторизован как "admin"
        //*     на сервере нет файлов
        //When  я делаю запрос на добавления файла
        //Then  сервер отвечает со статусом: 200
        //And   имя файла корректно
        String authToken = extractTokenFromResponse(loginAsAdmin());
        deleteAllIfExist(authToken);

        String filename = "smallTestFileOne.txt";
        ResponseEntity<FilesDto> response = addFileOnCloud(authToken, filename);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() != null && response.getBody().getFilename().equals(filename));
    }

    @Test
    public void successfullyPutFile() {
        //Scenario: Обновление файла с валидным токеном проходит успешно
        //Given я авторизован как "admin"
        //*     на сервере нет файлов
        //*     на сервер загружен файл с именем "smallTestFileTwo.txt"
        //When  я меняю имя файла на "helloWorld.txt"
        //Then  сервер отвечает со статусом: 200
        //And   в ответе имя файла соответствует ожидаемому
        //*     вес файла НЕ изменился
        String authToken = extractTokenFromResponse(loginAsAdmin());
        deleteAllIfExist(authToken);

        String filename = "smallTestFileTwo.txt";
        ResponseEntity<FilesDto> res = addFileOnCloud(authToken, filename);
        FilesDto currentFile = res.getBody();
        String newFilename = "helloWorld.txt";

        putFileName(authToken, filename, newFilename);

        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        assertEquals(newFilename, response.getBody().get(0).getFilename());
        if (currentFile != null) {
            assertEquals(response.getBody().get(0).getSize(), currentFile.getSize());
        }
    }

    @Test
    public void successfullyDeleteFile() {
        //Scenario: Удаление файла с валидным токеном проходит успешно
        //Given я авторизован как "admin"
        //*     на сервере нет файлов
        //*     на сервер загружен файл с именем "smallTestFileThree.txt"
        //When  я удаляю файл с именем "smallTestFileThree.txt"
        //Then  сервер отвечает со статусом: 200
        //And   файла с таким именем больше нет на сервере
        String authToken = extractTokenFromResponse(loginAsAdmin());
        deleteAllIfExist(authToken);
        String filename = "smallTestFileThree.txt";
        addFileOnCloud(authToken, filename);

        deleteFile(authToken, filename);
        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void successfullyAddThreeFiles() {
        //Scenario: C валидным токеном можно добавить несколько файлов
        //Given я авторизован как "admin"
        //*     на сервере нет файлов
        //When  я последовательно добавляю на сервер три файла
        //And   все 3 файла есть на сервере
        String authToken = extractTokenFromResponse(loginAsAdmin());
        deleteAllIfExist(authToken);

        String[] filenames = new String[]{"smallTestFileOne.txt", "smallTestFileTwo.txt", "smallTestFileThree.txt"};

        for (String filename: filenames) {
            addFileOnCloud(authToken, filename);
        }

        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(filenames.length, response.getBody().size());
    }

    public static ResponseEntity<List<FilesDto>> getFilesList(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_TOKEN_HEADER, "Bearer " + authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Выполняем запрос
        return restTemplate.exchange(
                "http://10.10.10.61:8084/list?limit=3",
                GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<FilesDto> addFileOnCloud(String authToken, String filename) {
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

    private void putFileName(String authToken, String filename, String newFilename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_TOKEN_HEADER, "Bearer " + authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateFilenameDto body = new UpdateFilenameDto(newFilename);

        HttpEntity<UpdateFilenameDto> entity = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                "http://10.10.10.61:8084/file?filename=" + filename,
                PUT,
                entity,
                Void.class
        );
    }

    private void deleteFile(String authToken, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_TOKEN_HEADER, "Bearer " + authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                "http://10.10.10.61:8084/file?filename=" + filename,
                DELETE,
                entity,
                Void.class
        );
    }

    private void deleteAllIfExist(String authToken) {
        ResponseEntity<List<FilesDto>> response = getFilesList(authToken);

        if (response.getBody() == null || response.getBody().isEmpty()) {
            System.out.println("Нет файлов чтобы удалять");

            return;
        }

        List<FilesDto> files = response.getBody();
        for (FilesDto file: files) {
            String filename = file.getFilename();
            System.out.println("Удаляем файл: " + filename);

            deleteFile(authToken, filename);
        }
    }
}
