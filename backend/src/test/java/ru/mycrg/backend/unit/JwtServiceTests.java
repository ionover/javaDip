package ru.mycrg.backend.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mycrg.backend.dto.UserDto;
import ru.mycrg.backend.entity.UsersEntity;
import ru.mycrg.backend.repository.UserRepository;
import ru.mycrg.backend.service.JwtService;
import ru.mycrg.backend.service.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtService jwtService;

    @InjectMocks
    private UserService userServiceForTest;

    @Test
    void testGenerateToken_ShouldReturnNonNullToken() {
        // Given
        String login = "testUser";
        String userId = "123";

        // When
        String token = jwtService.generateToken(login, userId);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50);
    }

    @Test
    void testGenerateToken_WithDifferentInputs_ShouldReturnDifferentTokens() {
        // Given
        String login1 = "user1";
        String userId1 = "123";
        String login2 = "user2";
        String userId2 = "456";

        // When
        String token1 = jwtService.generateToken(login1, userId1);
        String token2 = jwtService.generateToken(login2, userId2);

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }

    @Test
    void testUserService_FindByLoginAndPassword_WithMock() {
        // Given
        String login = "testUser";
        String password = "testPassword";

        UsersEntity userEntity = new UsersEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        userEntity.setJwtToken("some-token");

        when(userRepository.findByLoginAndPassword(login, password))
                .thenReturn(Optional.of(userEntity));

        // When
        Optional<UserDto> result = userServiceForTest.findByLoginAndPassword(login, password);

        // Then
        assertTrue(result.isPresent());
        assertEquals(login, result.get().getLogin());

        // Проверяем, что мок, был вызван ровно 1 раз
        verify(userRepository, times(1)).findByLoginAndPassword(login, password);
    }
}
