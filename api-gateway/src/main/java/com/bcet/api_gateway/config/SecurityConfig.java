package com.bcet.api_gateway.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").permitAll()
                        .anyExchange().permitAll())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Allowed origins
        corsConfig.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods
        corsConfig.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers
        corsConfig.setExposedHeaders(Arrays.asList("Authorization")); // Expose specific headers
        corsConfig.setAllowCredentials(true); // Allow credentials
        corsConfig.setMaxAge(3600L); // Cache duration for pre-flight requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply the CORS config to all routes
        return source;
    }
}
