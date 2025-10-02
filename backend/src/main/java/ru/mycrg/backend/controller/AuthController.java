package ru.mycrg.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.LoginDto;
import ru.mycrg.backend.dto.UserDto;
import ru.mycrg.backend.service.JwtService;
import ru.mycrg.backend.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        log.info("Пользователь: {} попытка авторизации", loginDto.getLogin());

        Optional<UserDto> userDto = userService.findByLoginAndPassword(
                loginDto.getLogin(),
                loginDto.getPassword()
        );

        if (userDto.isPresent()) {
            UserDto user = userDto.get();
            String token = jwtService.generateToken(user.getLogin(), user.getId().toString());

            user.setJwtToken(token);
            userService.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("auth-token", token);

            log.info("Пользователь: {} успешная авторизации", loginDto.getLogin());
            return ResponseEntity.ok(response);
        } else {
            log.info("Пользователь: {} Неправильный логин либо пароль!!!", loginDto.getLogin());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Bad credentials");
            errorResponse.put("id", 0);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        Optional<UserDto> userDto = userService.findByJwtToken(authToken);
        if (userDto.isPresent()) {
            UserDto user = userDto.get();
            user.setJwtToken(null);

            userService.save(user);
        }

        return ResponseEntity.ok().build();
    }
}
