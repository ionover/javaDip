package ru.mycrg.backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.mycrg.backend.dto.UserDto;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final Long JWT_EXPIRATION;

    private final SecretKey key;
    private final UserService userService;

    @Autowired
    public JwtService(Environment environment, UserService userService) {
        String secretKey = environment.getRequiredProperty("back-options.jwtSecretKey");
        JWT_EXPIRATION = Long.valueOf(environment.getRequiredProperty("back-options.jwtExpiration"));

        this.userService = userService;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String login, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login", login);
        claims.put("userId", userId);

        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(login)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }

    public boolean isTokenValid(String token) {
        token = token.trim();
        // Убираем префикс "Bearer " если он есть
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        log.info("Проверяем валидность токена: {}", token);

        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            log.info("Токен предоставленный пользователем сформирован нашим кодом.");

            Optional<UserDto> user = userService.findByJwtToken(token);

            if (user.isPresent()) {
                log.info("Токен найден в базе данных для пользователя: {}", user.get().getLogin());

                return true;
            } else {
                log.warn("Токен не найден в базе данных!!!");

                return false;
            }
        } catch (Exception e) {
            log.error("Ошибка валидации токена: {}", e.getMessage(), e);

            return false;
        }
    }
}
