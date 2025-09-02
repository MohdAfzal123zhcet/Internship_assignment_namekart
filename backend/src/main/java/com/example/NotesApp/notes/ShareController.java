package com.example.NotesApp.notes;

import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/share") @CrossOrigin
public class ShareController {
    private final ShareService share;

    public ShareController(ShareService s) {
        this.share = s;
    }

    @GetMapping("/{token}")
    public NoteDtos.NoteView view(@PathVariable String token) {
        return share.resolvePublic(token);
    }
}
