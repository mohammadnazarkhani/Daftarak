package com.example.daftarak.data.dao;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Note>> getAllNotesSortedByDateAsc();

    // Get all notes sorted by created_at descending
    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    LiveData<List<Note>> getAllNotesSortedByDateDesc();

    // Get all notes sorted by title ascending
    @Query("SELECT * FROM notes ORDER BY title ASC")
    LiveData<List<Note>> getAllNotesSortedByTitleAsc();

    // Get all notes sorted by title descending
    @Query("SELECT * FROM notes ORDER BY title DESC")
    LiveData<List<Note>> getAllNotesSortedByTitleDesc();

    // Search notes where title contains search text
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByTitle(String searchText);

    // Search notes where body contains search text
    @Query("SELECT * FROM notes WHERE body LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByBody(String searchText);

    // Search notes where title or body contains search text
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%' OR body LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByTitleOrBody(String searchText);

    // Get notes by notebook id
    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY created_at DESC")
    LiveData<List<Note>> getNotesByNotebookId(int notebookId);
}