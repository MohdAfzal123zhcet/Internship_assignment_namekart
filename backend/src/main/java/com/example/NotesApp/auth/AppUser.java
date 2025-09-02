package com.example.NotesApp.auth;
import jakarta.persistence.*; import java.time.Instant;
import java.util.UUID;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection; import org.springframework.security.core.GrantedAuthority;

@Entity @Table(name="app_user")
public class AppUser implements UserDetails {
    @Id private String id = UUID.randomUUID().toString();
    @Column(unique=true, nullable=false) private String email;
    @Column(nullable=false) private String passwordHash;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private Instant createdAt = Instant.now();

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // getters/setters
    @Override public Collection<? extends GrantedAuthority> getAuthorities(){ return java.util.List.of(); }
    @Override public String getPassword(){ return passwordHash; }
    @Override public String getUsername(){ return email; }
    @Override public boolean isAccountNonExpired(){ return true; }
    @Override public boolean isAccountNonLocked(){ return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled(){ return true; }
}