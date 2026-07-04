package com.studentexpense.tracker.controller;

import com.studentexpense.tracker.dto.AuthResponse;
import com.studentexpense.tracker.dto.LoginRequest;
import com.studentexpense.tracker.dto.RegisterRequest;
import com.studentexpense.tracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register -> create the (single) user account, returns a token immediately
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // POST /api/auth/login -> verify credentials, issue a fresh token
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
