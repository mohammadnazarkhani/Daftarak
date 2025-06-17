package com.example.daftarak.data.repository;

import android.content.Context;

import com.example.daftarak.data.dao.NoteDao;
import com.example.daftarak.data.database.AppDatabase;
import com.example.daftarak.data.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final ExecutorService executorService;

    public NoteRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        noteDao = db.noteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertNote(Note note) {
        executorService.execute(() -> noteDao.insertNote(note));
    }

    public void updateNote(Note note) {
        executorService.execute(() -> noteDao.updateNote(note));
    }

    public void deleteNote(Note note) {
        executorService.execute(() -> noteDao.deleteNote(note));
    }

    // Fetching sorted notes (sync — call in background thread)
    public List<Note> getNotesSortedByDateAsc() {
        return noteDao.getAllNotesSortedByDateAsc();
    }

    public List<Note> getNotesSortedByDateDesc() {
        return noteDao.getAllNotesSortedByDateDesc();
    }

    public List<Note> getNotesSortedByTitleAsc() {
        return noteDao.getAllNotesSortedByTitleAsc();
    }

    public List<Note> getNotesSortedByTitleDesc() {
        return noteDao.getAllNotesSortedByTitleDesc();
    }

    // Search
    public List<Note> searchNotesByTitle(String query) {
        return noteDao.searchNotesByTitle(query);
    }

    public List<Note> searchNotesByBody(String query) {
        return noteDao.searchNotesByBody(query);
    }

    public List<Note> searchNotesByTitleOrBody(String query) {
        return noteDao.searchNotesByTitleOrBody(query);
    }

    public List<Note> getNotesByNotebookId(int notebookId) {
        return noteDao.getNotesByNotebookId(notebookId);
    }
}
