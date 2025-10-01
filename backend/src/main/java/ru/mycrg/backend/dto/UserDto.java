package ru.mycrg.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mycrg.backend.entity.UsersEntity;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    UUID id;
    String login;
    String jwtToken;

    public UserDto(UsersEntity user) {
        this.id = user.getId();
        this.login = user.getLogin();
    }
}
