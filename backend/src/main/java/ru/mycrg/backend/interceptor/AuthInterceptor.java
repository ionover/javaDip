package ru.mycrg.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.mycrg.backend.service.JwtService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Пропускаем OPTIONS запросы (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // Пропускаем /login endpoint
        if (request.getRequestURI().endsWith("/login")) {
            return true;
        }

        String token = request.getHeader("auth-token");
        
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Missing auth-token header\",\"id\":401}");

            return false;
        }

        if (!jwtService.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid or expired token\",\"id\":401}");

            return false;
        }

        // Добавляем информацию о пользователе в request для дальнейшего использования
        request.setAttribute("userLogin", jwtService.getLoginFromToken(token));
        request.setAttribute("userId", jwtService.getUserIdFromToken(token));

        return true;
    }
}