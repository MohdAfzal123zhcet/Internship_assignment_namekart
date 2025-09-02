package com.example.NotesApp.notes;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List; import java.util.UUID;


@Service
public class NoteService {
    private final NoteRepository repo; private final ObjectMapper om=new ObjectMapper();
    public NoteService(NoteRepository repo){ this.repo=repo; }
    public List<NoteDtos.NoteView> list(String userId){ return repo.findByUserIdOrderByUpdatedAtDesc(userId).stream().map(this::toView).toList(); }
    public NoteDtos.NoteView getOwned(String id, String userId){ var n=repo.findById(id).orElseThrow(); if(!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden"); return toView(n); }
    @Transactional public NoteDtos.NoteView create(String userId, NoteDtos.NoteCreateReq req){ var n=new Note(); n.setUserId(userId); n.setTitle(req.title()); n.setContent(req.content()); n.setTagsJson(writeTags(req.tags())); n.setUpdatedAt(Instant.now()); return toView(repo.save(n)); }
    @Transactional public NoteDtos.NoteView update(String id, String userId, NoteDtos.NoteUpdateReq req){ var n=repo.findById(id).orElseThrow(); if(!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden"); if(n.getVersion()!=req.version()) throw new VersionConflictException(n.getVersion()); if(req.title()!=null) n.setTitle(req.title()); if(req.content()!=null) n.setContent(req.content()); if(req.tags()!=null) n.setTagsJson(writeTags(req.tags())); n.setVersion(n.getVersion()+1); n.setUpdatedAt(Instant.now()); return toView(n); }
    @Transactional public void delete(String id, String userId){ var n=repo.findById(id).orElseThrow(); if(!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden"); repo.delete(n); }
    private NoteDtos.NoteView toView(Note n){ return new NoteDtos.NoteView(n.getId(), n.getTitle(), n.getContent(), readTags(n.getTagsJson()), n.getUpdatedAt(), n.getVersion()); }
    private List<String> readTags(String json){ try { return om.readValue(json, new TypeReference<List<String>>(){});} catch(Exception e){ return List.of(); } }
    private String writeTags(List<String> tags){ try { return om.writeValueAsString(tags==null?List.of():tags);} catch(Exception e){ return "[]"; } }
    public static class VersionConflictException extends RuntimeException { public final int serverVersion; public VersionConflictException(int v){ super("Version conflict"); this.serverVersion=v; } }
}
