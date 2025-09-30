package ru.mycrg.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.LoginDto;
import ru.mycrg.backend.dto.UserDto;
import ru.mycrg.backend.exception.AuthException;
import ru.mycrg.backend.service.JwtService;
import ru.mycrg.backend.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        Optional<UserDto> userDto = userService.findByLoginAndPassword(
                loginDto.getLogin(),
                loginDto.getPassword()
        );

        if (userDto.isPresent()) {
            UserDto user = userDto.get();
            String token = jwtService.generateToken(user.getLogin(), user.getId().toString());

            user.setJwtToken(token);
            userService.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("auth-token", token);

            return ResponseEntity.ok(response);
        } else {
            throw new AuthException("Invalid login or password");
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader String token) {
        return "Hello World";
    }
}
