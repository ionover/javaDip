package ru.mycrg.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.backend.dto.LoginDto;

@RestController
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        String token = "";

        return token;
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader String token) {
        return "Hello World";
    }
}
