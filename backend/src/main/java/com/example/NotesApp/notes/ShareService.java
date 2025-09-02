package com.example.NotesApp.notes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URLEncoder; import java.nio.charset.StandardCharsets;
import java.time.Instant; import java.util.UUID;


@Service
public class ShareService {
    private final ShareLinkRepository shares; private final NoteRepository notes;
    public ShareService(ShareLinkRepository shares, NoteRepository notes){ this.shares=shares; this.notes=notes; }


    @Transactional
    public NoteDtos.ShareResp createOrGet(String noteId, String baseUrl, Long expiresInSeconds, String userId){
        var n=notes.findById(noteId).orElseThrow(); if(!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden");
        var existing=shares.findById(noteId).orElse(null);
        if(existing!=null && (existing.getExpiresAt()==null || existing.getExpiresAt().isAfter(Instant.now())))
            return toResp(existing, baseUrl);
        var s=new ShareLink(); s.setNoteId(noteId); s.setToken(UUID.randomUUID().toString().replace("-",""));
        s.setExpiresAt(expiresInSeconds==null?null:Instant.now().plusSeconds(expiresInSeconds));
        shares.save(s); return toResp(s, baseUrl);
    }
    @Transactional public void revoke(String noteId, String userId){ var n=notes.findById(noteId).orElseThrow(); if(!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden"); shares.deleteById(noteId); }
    public NoteDtos.NoteView resolvePublic(String token)
    {
        var s=shares.findByToken(token).orElseThrow();
        if(s.getExpiresAt()!=null && s.getExpiresAt().isBefore(Instant.now())) throw new RuntimeException("Link expired");
        var n=notes.findById(s.getNoteId()).orElseThrow();
        return new NoteDtos.NoteView(n.getId(), n.getTitle(), n.getContent(), java.util.List.of(), n.getUpdatedAt(), n.getVersion()); }
    private NoteDtos.ShareResp toResp(ShareLink s, String base){ String url=base+"/s/"+ URLEncoder.encode(s.getToken(), StandardCharsets.UTF_8); return new NoteDtos.ShareResp(s.getToken(), url, s.getExpiresAt()); }
}
