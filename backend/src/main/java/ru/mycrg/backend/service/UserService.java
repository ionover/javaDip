package ru.mycrg.backend.service;

import org.springframework.stereotype.Service;
import ru.mycrg.backend.dto.UserDto;
import ru.mycrg.backend.entity.UsersEntity;
import ru.mycrg.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> findByLoginAndPassword(String login, String password) {
        Optional<UsersEntity> usersEntity = userRepository.findByLoginAndPassword(login, password);

        return usersEntity.map(UserDto::new);
    }

    public void save(Optional<UserDto> userDto) {
        UserDto userDtoToSave = null;

        if (userDto.isPresent()) {
            userDtoToSave = userDto.get();
        }

        UserRepository user = userRepository.save(userDtoToSave);
    }
}
