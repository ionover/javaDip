package ru.mycrg.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return login(username, password);
    }

    @PostMapping("/logout")
    public String logout() {
        return "Hello World";
    }
}
