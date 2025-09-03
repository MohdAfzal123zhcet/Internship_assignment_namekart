package com.example.NotesApp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> auth
                        // Preflight (allowed; ** yahan END pe hai, isliye valid)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Static resources from classpath: /static/**, /public/**, etc.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // Root pages (agar serve kar rahe ho)
                        .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()

                        // Health / error
                        .requestMatchers("/actuator/health", "/actuator/health/**", "/error").permitAll()

                        // Public APIs
                        .requestMatchers("/api/v1/auth/**", "/api/v1/share/**").permitAll()

                        // Everything else
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS for frontend origins
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // If you're not using cookies, you can set this to false safely.
        // Keeping true only if you actually need cross-site cookies.
        config.setAllowCredentials(true);

        // Allow your local dev & Vercel preview/prod
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "https://internship-assignment-namekart.vercel.app/"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // This is fine; pattern ends at /** (valid)
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
