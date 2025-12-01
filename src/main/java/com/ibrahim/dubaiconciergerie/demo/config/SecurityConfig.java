package com.ibrahim.dubaiconciergerie.demo.config;

import com.ibrahim.dubaiconciergerie.demo.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Pour une API Angular, on désactive CSRF au début
                .csrf(csrf -> csrf.disable())
                // CORS: autoriser Angular (4200) à appeler l’API (8080)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        // On ouvre les endpoints d'API pour l'instant
                        .requestMatchers("/api/owner/bookings/**", "/api/owner/bookings").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        // le reste on peut aussi ouvrir pour l’instant
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
