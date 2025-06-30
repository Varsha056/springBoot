package com.example.userAccess.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    // ✅ Inject your custom JwtFilter
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 🔒 Define Spring Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ❌ Disable CSRF (not needed for token-based auth)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 🌐 Enable CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 🚫 No sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/login",
                                "/api/register",
                                "/login.html",
                                "/register.html",
                                "/home.html",
                                "/js/**",
                                "/css/**"
                        ).permitAll() // 🟢 Public endpoints
                        .anyRequest().authenticated() // 🔒 Secure everything else
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // 🔑 Add JWT filter

        return http.build();
    }

    // 🔐 Password encoder for hashing passwords (e.g. BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌐 CORS config so frontend can call backend APIs
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080")); // ✅ Allow frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Allow HTTP methods
        config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // ✅ Allow token + JSON headers
        config.setAllowCredentials(true); // ✅ Allow sending cookies/credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 🔁 Apply to all routes
        return source;
    }
}
