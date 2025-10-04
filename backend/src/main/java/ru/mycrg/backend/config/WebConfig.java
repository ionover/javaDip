package ru.mycrg.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.mycrg.backend.interceptor.AuthInterceptor;

@Configuration
@EnableWebMvc
class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final String allowedOrigin;

    WebConfig(Environment environment, AuthInterceptor authInterceptor) {
        allowedOrigin = environment.getRequiredProperty("back-options.allowedOrigin");

        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
