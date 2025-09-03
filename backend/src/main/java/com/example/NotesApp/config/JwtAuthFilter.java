package com.example.NotesApp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtService jwt, UserDetailsService uds) {
        this.jwt = jwt;
        this.uds = uds;
    }

    // ✅ Whitelisted paths where no JWT is required
    private static final List<String> WHITELIST = List.of(
            "/", "/index.html", "/favicon.ico",
            "/actuator/health", "/actuator/health/**",
            "/api/v1/auth/**",
            "/api/v1/share/**"
    );

    private static final AntPathMatcher PATHS = new AntPathMatcher();

    private boolean isWhitelisted(String uri) {
        for (String pattern : WHITELIST) {
            if (PATHS.match(pattern, uri)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        final String uri = req.getRequestURI();

        // 1) Always allow CORS preflight and whitelisted routes
        if ("OPTIONS".equalsIgnoreCase(req.getMethod()) || isWhitelisted(uri)) {
            chain.doFilter(req, res);
            return;
        }

        // 2) For other routes, only process JWT if header present
        String h = req.getHeader("Authorization");
        if (h == null || !h.startsWith("Bearer ")) {
            // No token → continue; Spring Security will enforce auth on secured routes
            chain.doFilter(req, res);
            return;
        }

        String token = h.substring(7);
        String username = null;
        try {
            username = jwt.extractUsername(token);
        } catch (Exception e) {
            // Malformed token → 401
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ud = uds.loadUserByUsername(username);
            if (jwt.isTokenValid(token, ud)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
