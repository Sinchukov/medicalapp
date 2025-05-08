package com.medicalapp.config;

import com.medicalapp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
// Включаем @PreAuthorize и подобные аннотации
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
     * BCrypt‐шифратор паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager для аутентификации в AuthController
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

    /**
     * Конфигурация цепочки фильтров Spring Security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // отключаем CSRF (т.к. SPA/JS клиент)
                .csrf().disable()

                // без сессий — чисто JWT
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // правила доступа
                .authorizeRequests(authorize -> authorize
                        // публичные эндпоинты для логина/регистрации
                        .antMatchers("/api/auth/**").permitAll()
                        // эндпоинт выдачи рецептов только для роли PHARMACY
                        .antMatchers(HttpMethod.POST, "/api/pharmacy/prescriptions/*/dispense")
                        .hasRole("PHARMACY")
                        // разрешаем статику и html
                        .antMatchers(
                                "/", "/*.html", "/**/*.html",
                                "/**/*.css", "/**/*.js",
                                "/favicon.ico", "/webjars/**"
                        ).permitAll()
                        // остальные запросы — только авторизованные
                        .anyRequest().authenticated()
                )

                // наш фильтр JWT до стандартного UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // для удобства оставим HTTP Basic (при желании можно удалить)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
