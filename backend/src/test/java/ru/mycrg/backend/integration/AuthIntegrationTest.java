//package ru.mycrg.backend.integration;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
//import ru.mycrg.backend.dto.LoginDto;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Testcontainers
//public class AuthIntegrationTest {
//
//    @Test
//    void successfullyAuthWithCorrectCredentials() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        LoginDto requestBody = new LoginDto("admin", "admin");
//
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//        // Выполняем запрос
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                "http://10.10.10.61:8084/login",
//                entity,
//                String.class
//        );
//
//        // Проверяем результат
//        assertTrue(response.getStatusCode().is2xxSuccessful());
//        assertTrue(response.getBody() != null && response.getBody().contains("auth-token"));
//    }
//}
