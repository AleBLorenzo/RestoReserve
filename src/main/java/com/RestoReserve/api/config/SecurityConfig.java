package com.RestoReserve.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Públicos
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Tables - solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/v1/tables").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/tables").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/tables/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tables/**").hasAuthority("ADMIN")
                
                //Usuario - solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("ADMIN")
                // Reservations - autenticados
                .requestMatchers("/api/v1/reservations/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}