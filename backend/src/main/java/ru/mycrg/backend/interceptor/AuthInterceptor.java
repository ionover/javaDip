package ru.mycrg.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.mycrg.backend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("AuthInterceptor: {} {}", request.getMethod(), request.getRequestURI());
        
        // Пропускаем OPTIONS запросы (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            logger.info("Skipping OPTIONS request");
            return true;
        }

        // Пропускаем /login endpoint
        if (request.getRequestURI().endsWith("/login")) {
            logger.info("Skipping /login endpoint");
            return true;
        }

        String token = request.getHeader("auth-token");
        logger.info("Auth token: {}", token != null ? "present" : "missing");

        if (token == null || token.isEmpty()) {
            logger.warn("Missing auth-token header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Missing auth-token header\",\"id\":401}");

            return false;
        }

        boolean isValid = jwtService.isTokenValid(token);
        logger.info("Token validation result: {}", isValid);
        return isValid;
    }
}