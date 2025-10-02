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
import ru.mycrg.backend.dto.response.LoginSuccessResponse;
import ru.mycrg.backend.exception.UserNotFoundException;
import ru.mycrg.backend.service.JwtService;
import ru.mycrg.backend.service.UserService;

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
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
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
            log.info("Пользователь: {} успешная авторизации", loginDto.getLogin());

            return ResponseEntity.ok(new LoginSuccessResponse(token));
        } else {
            log.info("Пользователь: {} Неправильный логин либо пароль!!!", loginDto.getLogin());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new UserNotFoundException("Bad credentials", 0));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        Optional<UserDto> userDto = userService.findByJwtToken(authToken);
        if (userDto.isPresent()) {
            UserDto user = userDto.get();

            log.info("Пользователь: {} попытка выйти.", user.getLogin());

            user.setJwtToken(null);
            userService.save(user);
        } else {
            log.info("Была попытка выйти со старым токеном: {}", authToken);
        }

        return ResponseEntity.ok().build();
    }
}
