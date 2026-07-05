package com.studentexpense.tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Without this, Spring Security's default behavior returns an empty 403
 * for ANY unauthenticated request (missing token, expired token, garbage
 * token) instead of a proper 401 with a useful error body. This makes every
 * "you're not logged in" case consistent: always 401, always the same JSON
 * shape as GlobalExceptionHandler uses elsewhere.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Injected, NOT "new ObjectMapper()" — Spring Boot's autoconfigured
    // ObjectMapper has the JavaTimeModule registered, so it knows how to
    // serialize LocalDateTime. A manually constructed one doesn't, and
    // throws mid-write, silently truncating the response body.
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", "Missing or invalid authentication token");

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
