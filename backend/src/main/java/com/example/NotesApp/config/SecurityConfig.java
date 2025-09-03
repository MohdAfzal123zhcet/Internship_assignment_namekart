package com.example.NotesApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider,
                                           JwtAuthFilter jwtFilter) throws Exception {
        http
                .csrf(cs -> cs.disable())
                .cors(Customizer.withDefaults()) // 👈 CORS enabled (uses bean below)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> auth
                        // preflight ko allow kar do
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // public endpoints
                        .requestMatchers("/api/v1/auth/**", "/api/v1/share/**").permitAll()
                        // baaki sab secured
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 👇 CORS rules (frontend origin allow)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // apna frontend origin yahan dal do
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://notes-frontend.vercel.app" // apna Vercel domain
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply to all paths
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
