package ru.mycrg.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.LoginDto;
import ru.mycrg.backend.entity.UsersEntity;
import ru.mycrg.backend.repository.UserRepository;
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
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Optional<UsersEntity> userOpt = userService.findByLoginAndPassword(
                loginDto.getLogin(),
                loginDto.getPassword()
        );

        if (userOpt.isPresent()) {
            UsersEntity user = userOpt.get();
            String token = jwtService.generateToken(user.getLogin(), user.getId().toString());

            user.setJwtToken(token);
            userService.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("auth-token", token);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Bad credentials");
            errorResponse.put("id", 400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader String token) {
        return "Hello World";
    }
}
