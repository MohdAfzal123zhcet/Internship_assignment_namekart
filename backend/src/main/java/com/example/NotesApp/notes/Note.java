package com.example.NotesApp.notes;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "note")
public class Note {

    // ---- Identity ----
    @Id
    private String id = UUID.randomUUID().toString();

    // ---- Ownership ----
    @Column(nullable = false)
    private String userId;

    // ---- Content ----
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")   // removed @Lob
    private String content;

    // ---- Tags (JSONB) ----
    @Column(name = "tags_json", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags = new ArrayList<>();

    // ---- Tracking ----
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Version
    @Column(nullable = false)
    private int version;   // removed "= 1", let Hibernate manage

    // ---- Lifecycle hooks ----
    @PrePersist
    void onCreate() {
        if (updatedAt == null) updatedAt = Instant.now();
        if (id == null || id.isBlank()) id = UUID.randomUUID().toString();
        if (tags == null) tags = new ArrayList<>();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
        if (tags == null) tags = new ArrayList<>();
    }

    // ---- Getters / Setters ----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = (tags != null) ? tags : new ArrayList<>(); }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}
