package ru.mycrg.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidation123456789";
    private static final long JWT_EXPIRATION = 86400000;

    private final SecretKey key;

    public JwtService() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
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
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        return claims.get("login", String.class);
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        return claims.get("userId", String.class);
    }
}