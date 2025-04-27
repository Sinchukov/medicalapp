package com.medicalapp.controller;

import com.medicalapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String companyName) {
        authService.register(role, email, password, companyName);
        return ResponseEntity.ok(Map.of("status", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(
                Collections.singletonMap("token", token)
        );
    }
}
