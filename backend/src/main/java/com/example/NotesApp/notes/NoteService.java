package com.example.NotesApp.notes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    /** List notes for a user, newest first */
    public List<NoteDtos.NoteView> list(String userId) {
        return repo.findByUserIdOrderByUpdatedAtDesc(userId)
                .stream()
                .map(this::toView)
                .toList();
    }

    /** Get a single note (and verify ownership) */
    public NoteDtos.NoteView getOwned(String id, String userId) {
        var n = repo.findById(id).orElseThrow();
        if (!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden");
        return toView(n);
    }

    /** Create a new note */
    @Transactional
    public NoteDtos.NoteView create(String userId, NoteDtos.NoteCreateReq req) {
        var n = new Note();
        n.setUserId(userId);
        n.setTitle(req.title());
        n.setContent(req.content());
        // Note entity has List<String> tags; set empty list if null
        n.setTags(req.tags() != null ? req.tags() : List.of());
        // updatedAt handled by @UpdateTimestamp too, but safe to set now
        n.setUpdatedAt(Instant.now());
        n = repo.save(n);
        return toView(n);
    }

    /** Update an existing note with optimistic locking (version check) */
    @Transactional
    public NoteDtos.NoteView update(String id, String userId, NoteDtos.NoteUpdateReq req) {
        var n = repo.findById(id).orElseThrow();
        if (!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden");

        // Optimistic lock: compare client version vs current
        if (n.getVersion() != req.version()) {
            throw new VersionConflictException(n.getVersion());
        }

        if (req.title() != null)   n.setTitle(req.title());
        if (req.content() != null) n.setContent(req.content());
        if (req.tags() != null)    n.setTags(req.tags());

        // Do NOT manually bump version when using @Version;
        // Hibernate will increment on flush/commit.
        n.setUpdatedAt(Instant.now());

        // No explicit save needed due to dirty checking, but harmless if you prefer:
        // n = repo.save(n);

        return toView(n);
    }

    /** Delete a note (ownership enforced) */
    @Transactional
    public void delete(String id, String userId) {
        var n = repo.findById(id).orElseThrow();
        if (!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden");
        repo.delete(n);
    }

    /** Map entity -> DTO */
    private NoteDtos.NoteView toView(Note n) {
        return new NoteDtos.NoteView(
                n.getId(),
                n.getTitle(),
                n.getContent(),
                n.getTags() != null ? n.getTags() : List.of(),
                n.getUpdatedAt(),
                n.getVersion()
        );
    }

    /** Thrown on optimistic locking conflict */
    public static class VersionConflictException extends RuntimeException {
        public final int serverVersion;
        public VersionConflictException(int v) {
            super("Version conflict");
            this.serverVersion = v;
        }
    }
}
