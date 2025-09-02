package com.example.NotesApp.notes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

public class NoteDtos {

    public record NoteCreateReq(String title, String content, List<String> tags){}
    public record NoteUpdateReq(String title, String content, List<String> tags, int version){}
    public record NoteView(String id, String title, String content, List<String> tags, Instant updatedAt, int version){}
    public record ShareReq(Long expiresInSeconds){}
    public record ShareResp(String token, String url, Instant expiresAt){}
}
