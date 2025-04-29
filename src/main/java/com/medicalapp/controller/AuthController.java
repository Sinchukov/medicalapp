// src/main/java/com/medicalapp/controller/AuthController.java
package com.medicalapp.controller;

import com.medicalapp.dto.AuthRequest;
import com.medicalapp.dto.RegisterDto;
import com.medicalapp.security.JwtTokenProvider;
import com.medicalapp.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtProvider;

    public AuthController(AuthService as,
                          AuthenticationManager am,
                          JwtTokenProvider jp){
        this.authService  = as;
        this.authManager  = am;
        this.jwtProvider  = jp;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        try {
            authService.register(dto);
            return ResponseEntity.ok(Map.of("status","registered"));
        } catch (Exception ex) {
            log.error("Registration failed", ex);
            return ResponseEntity.status(500)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(), req.getPassword()
                )
        );
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a->a.getAuthority().replace("ROLE_",""))
                .orElse("");
        String token = jwtProvider.createToken(auth.getName(), role);
        return ResponseEntity.ok(Map.of("token",token, "role",role));
    }
}
