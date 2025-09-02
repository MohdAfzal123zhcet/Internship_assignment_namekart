package com.example.NotesApp.common;

import com.example.NotesApp.notes.NoteService.VersionConflictException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
record ErrorBody(String message, Integer serverVersion){}
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(VersionConflictException.class)
    public ResponseEntity<ErrorBody> handle(VersionConflictException e)
    { return ResponseEntity.status(409).body(new ErrorBody(e.getMessage(), e.serverVersion));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorBody> generic(RuntimeException e)
    { return ResponseEntity.badRequest().body(new ErrorBody(e.getMessage(), null));
    }
}