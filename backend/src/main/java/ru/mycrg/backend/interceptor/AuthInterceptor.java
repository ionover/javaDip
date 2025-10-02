package ru.mycrg.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.mycrg.backend.exception.AuthException;
import ru.mycrg.backend.service.JwtService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("AuthInterceptor: {} {}", request.getMethod(), request.getRequestURI());

        // Пропускаем OPTIONS запросы (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // Пропускаем /login endpoint (на всякий случай. Вообще он сюда не попадёт из-за конфига)
        if (request.getRequestURI().endsWith("/login")) {
            log.info("Skipping /login endpoint");

            return true;
        }

        String token = request.getHeader("auth-token");
        log.info("Auth token: {}", token != null ? "present" : "missing");

        if (token == null || token.isEmpty()) {
            log.warn("Missing auth-token header");

            throw new AuthException("Missing auth-token header");
        }

        boolean isValid = jwtService.isTokenValid(token);
        log.info("Token validation result: {}", isValid);

        return isValid;
    }
}