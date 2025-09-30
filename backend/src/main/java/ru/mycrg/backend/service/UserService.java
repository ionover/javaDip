package ru.mycrg.backend.service;

import org.springframework.stereotype.Service;
import ru.mycrg.backend.entity.UsersEntity;

import java.util.Optional;

@Service
public class UserService {

    private final UserService userService;

    public UserService(UserService userService) {
        this.userService = userService;
    }

    public Optional<UsersEntity> findByLoginAndPassword(String login, String password) {
        return userService.findByLoginAndPassword(login, password);
    }
}
