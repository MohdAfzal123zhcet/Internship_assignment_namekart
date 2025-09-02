package com.example.NotesApp.notes;

import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface NoteRepository extends JpaRepository<Note,String>
{
    List<Note> findByUserIdOrderByUpdatedAtDesc(String userId);
}
