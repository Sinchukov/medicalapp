package com.medicalapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Чтобы @PreAuthorize("hasAuthority('PHARMACY')") и др. заработали:
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService     = userDetailsService;
    }

    /**
     * PasswordEncoder — используем BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager для AuthController
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, т. к. наша клиентская часть — SPA/JS
                .csrf().disable()

                // Без сессий, всё по JWT
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Маршруты
                .authorizeRequests(authorize -> authorize
                        // публичные endpoiпты авторизации
                        .antMatchers("/api/auth/**").permitAll()
                        // фронтовые страницы и ресурсы
                        .antMatchers(
                                "/",
                                "/*.html",
                                "/**/*.html",
                                "/**/*.css",
                                "/**/*.js",
                                "/favicon.ico"
                        ).permitAll()
                        // всё прочее — только аутентифицированные
                        .anyRequest().authenticated()
                )

                // Вставляем свой фильтр проверки JWT
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                // По умолчанию используем HTTP Basic (для тестов). Можно убрать или заменить.
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
