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

    @Query("SELECT * FROM notes ORDER BY created_at ASC")
    LiveData<List<Note>> getAllNotesSortedByDateAsc();

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    LiveData<List<Note>> getAllNotesSortedByDateDesc();

    @Query("SELECT * FROM notes ORDER BY title ASC")
    LiveData<List<Note>> getAllNotesSortedByTitleAsc();

    @Query("SELECT * FROM notes ORDER BY title DESC")
    LiveData<List<Note>> getAllNotesSortedByTitleDesc();

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByTitle(String searchText);

    @Query("SELECT * FROM notes WHERE body LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByBody(String searchText);

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchText || '%' OR body LIKE '%' || :searchText || '%'")
    LiveData<List<Note>> searchNotesByTitleOrBody(String searchText);

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY created_at DESC")
    LiveData<List<Note>> getNotesByNotebookId(int notebookId);

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY title ASC")
    LiveData<List<Note>> getNotesByNotebookIdSortedByTitleAsc(int notebookId);

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY title DESC")
    LiveData<List<Note>> getNotesByNotebookIdSortedByTitleDesc(int notebookId);

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY created_at ASC")
    LiveData<List<Note>> getNotesByNotebookIdSortedByDateAsc(int notebookId);

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId ORDER BY created_at DESC")
    LiveData<List<Note>> getNotesByNotebookIdSortedByDateDesc(int notebookId);
}