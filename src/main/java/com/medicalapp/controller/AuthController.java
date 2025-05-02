// src/main/java/com/medicalapp/controller/AuthController.java
package com.medicalapp.controller;

import com.medicalapp.dto.LoginDto;
import com.medicalapp.dto.RegisterDto;
import com.medicalapp.service.AuthService;
import com.medicalapp.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(
            AuthService authService,
            AuthenticationManager authManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.authService      = authService;
        this.authManager      = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        authService.register(dto);
        // после регистрации клиенту скажем: зарегистрировано
        return ResponseEntity.ok(Map.of("status", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        // 1) Аутентифицируем
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        // 2) Генерим JWT
        String token = jwtTokenProvider.createToken(auth);
        // 3) Извлекаем роль (берём первую из Authority; в вашем случае их всего одна)
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        // обычно роли приходят как "ROLE_PATIENT" и т.п. — уберём префикс "ROLE_"
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        // 4) Возвращаем клиенту оба поля
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role",  role     // будет "PATIENT", "DOCTOR" или "PHARMACY"
        ));
    }
}
