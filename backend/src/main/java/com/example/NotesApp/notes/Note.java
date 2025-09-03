package com.example.NotesApp.notes;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "note")
public class Note {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")   // MEDIUMTEXT -> TEXT
    private String content;

    @Column(name = "tags_json", nullable = false, columnDefinition = "JSONB") // JSON -> JSONB
    private String tagsJson = "[]";

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(nullable = false)
    private int version = 1;

    // setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTagsJson(String tagsJson) { this.tagsJson = tagsJson; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setVersion(int version) { this.version = version; }

    // getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTagsJson() { return tagsJson; }
    public Instant getUpdatedAt() { return updatedAt; }
    public int getVersion() { return version; }
}
