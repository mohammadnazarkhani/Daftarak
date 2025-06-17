package com.example.daftarak.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.daftarak.data.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    // Get all notes sorted by created_at ascending
    @Query("SELECT * FROM notes ORDER BY created_at ASC")
    List<Note> getAllNotesSortedByDateAsc();

    // Get all notes sorted by created_at descending
    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    List<Note> getAllNotesSortedByDateDesc();

    // Get all notes sorted by title ascending
    @Query("SELECT * FROM notes ORDER BY title ASC")
    List<Note> getAllNotesSortedByTitleAsc();

    // Get all notes sorted by title descending
    @Query("SELECT * FROM notes ORDER BY title DESC")
    List<Note> getAllNotesSortedByTitleDesc();

    // Search notes where title contains search text (case insensitive)
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%'")
    List<Note> searchNotesByTitle(String searchText);

    // Search notes where body contains search text (case insensitive)
    @Query("SELECT * FROM notes WHERE body LIKE '%' || :searchText || '%'")
    List<Note> searchNotesByBody(String searchText);

    // Search notes where title or body contains search text (case insensitive)
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%' OR body LIKE '%' || :searchText || '%'")
    List<Note> searchNotesByTitleOrBody(String searchText);

    // Optional: Get notes by notebook id (to filter notes by notebook)
    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY created_at DESC")
    List<Note> getNotesByNotebookId(int notebookId);
}
