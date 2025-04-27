package com.medicalapp.controller;

import com.medicalapp.dto.AuthRequest;
import com.medicalapp.dto.RegisterDto;
import com.medicalapp.security.JwtTokenProvider;
import com.medicalapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtProvider;

    public AuthController(AuthService authService,
                          AuthenticationManager authManager,
                          JwtTokenProvider jwtProvider) {
        this.authService  = authService;
        this.authManager  = authManager;
        this.jwtProvider  = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        authService.register(dto);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );
        // извлекаем роль без префикса
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(ga -> ga.getAuthority().replace("ROLE_", ""))
                .orElse("");
        // создаём токен с именем пользователя и ролью
        String token = jwtProvider.createToken(auth.getName(), role);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role",  role
        ));
    }
}
