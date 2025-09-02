package com.example.NotesApp.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key; import java.util.Date; import java.util.Map;


@Service
public class JwtService {
    private final Key key; private static final long EXP=1000L*60*60*12; // 12h
    public JwtService(@Value("${app.jwt-secret}") String secret)
    { this.key=Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String generateToken(String username)
    { return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis()+EXP))
            .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    public String extractUsername(String token)
    { return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject(); }
    public boolean isTokenValid(String token, UserDetails ud){ try { String u=extractUsername(token); return u.equals(ud.getUsername()) && !isExpired(token);} catch(Exception e){ return false; } }
    private boolean isExpired(String token){ return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date()); }
}
