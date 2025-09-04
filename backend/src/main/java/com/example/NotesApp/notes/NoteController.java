package com.example.NotesApp.notes;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*; import com.example.NotesApp.auth.AppUser;
import java.util.List;


@RestController @RequestMapping("/api/v1") @CrossOrigin
public class NoteController {
    private final NoteService notes;
    private final ShareService share;
    public NoteController(NoteService n, ShareService s)
    { this.notes=n; this.share=s;

    }
    @GetMapping("/notes")
    public List<NoteDtos.NoteView> list(@AuthenticationPrincipal AppUser u)
    {
        return notes.list(u.getId());

    }
    @PostMapping("/notes")
    public ResponseEntity<NoteDtos.NoteView> create(@AuthenticationPrincipal AppUser u, @RequestBody NoteDtos.NoteCreateReq r)
    {
        return ResponseEntity.status(201).body(notes.create(u.getId(), r));

    }
    @GetMapping("/notes/{id}") public NoteDtos.NoteView get(@AuthenticationPrincipal AppUser u, @PathVariable String id)
    {
        return notes.getOwned(id, u.getId());
    }
    @PutMapping("/notes/{id}") public NoteDtos.NoteView update(@AuthenticationPrincipal AppUser u, @PathVariable String id, @RequestBody NoteDtos.NoteUpdateReq r)
    {
        return notes.update(id, u.getId(), r);
    }
    @DeleteMapping("/notes/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal AppUser u, @PathVariable String id)
    {
        notes.delete(id, u.getId()); return ResponseEntity.noContent().build();
    }
    @PostMapping("/notes/{id}/share") public ResponseEntity<NoteDtos.ShareResp> share(@AuthenticationPrincipal AppUser u, @PathVariable String id, @RequestBody(required=false) NoteDtos.ShareReq req, @RequestHeader(value="X-Base-Url", required=false) String base)
    {
        String b = (base==null||base.isBlank())?"http://localhost:5173":base; var resp=share.createOrGet(id,b, req==null?null:req.expiresInSeconds(), u.getId()); return ResponseEntity.status(201).body(resp);
    }
    @DeleteMapping("/notes/{id}/share") public ResponseEntity<Void> unshare(@AuthenticationPrincipal AppUser u, @PathVariable String id)
    {
        share.revoke(id,u.getId()); return ResponseEntity.noContent().build();
    }
}