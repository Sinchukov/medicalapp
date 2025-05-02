package com.medicalapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.medicalapp.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // Секретный ключ (желательно хранить в application.properties)
    private final String jwtSecret = "mySuperSecretKey";
    // Время жизни токена (например, 1 день)
    private final long jwtExpirationMs = 24 * 60 * 60 * 1000L;

    // Сервис для загрузки UserDetails по email
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Создание токена на основе Authentication
     */
    public String createToken(Authentication authentication) {
        String email = authentication.getName();

        // Собираем роли в одну строку через запятую
        String roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    /**
     * Валидация токена
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Извлечение email из токена (subject)
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Извлечение списка ролей из токена
     */
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        String roles = claims.get("roles", String.class);
        if (roles == null || roles.isBlank()) {
            return List.of();
        }

        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Преобразование токена в Authentication
     */
    public Authentication getAuthentication(String token) {
        // Получаем email из токена
        String email = getEmailFromToken(token);
        // Загружаем полные детали пользователя
        var userDetails = userDetailsService.loadUserByUsername(email);
        // Получаем роли из токена
        var authorities = getAuthoritiesFromToken(token);
        // Возвращаем объект Authentication
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * Извлечение токена из заголовка Authorization: Bearer ...
     */
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
