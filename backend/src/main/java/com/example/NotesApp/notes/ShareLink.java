package com.example.NotesApp.notes;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Entity @Table(name="share_link")
public class ShareLink {
    @Id @Column(name="note_id") private String noteId;
    @Column(nullable=false, unique=true) private String token;
    private Instant expiresAt; @Column(nullable=false) private Instant createdAt = Instant.now();

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
// getters/setters
}