package com.example.daftarak.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

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

    public LiveData<java.util.List<Note>> getNotesSortedByDateAsc() {
        return noteDao.getAllNotesSortedByDateAsc();
    }

    public LiveData<java.util.List<Note>> getNotesSortedByDateDesc() {
        return noteDao.getAllNotesSortedByDateDesc();
    }

    public LiveData<java.util.List<Note>> getNotesSortedByTitleAsc() {
        return noteDao.getAllNotesSortedByTitleAsc();
    }

    public LiveData<java.util.List<Note>> getNotesSortedByTitleDesc() {
        return noteDao.getAllNotesSortedByTitleDesc();
    }

    public LiveData<java.util.List<Note>> searchNotesByTitle(String query) {
        return noteDao.searchNotesByTitle(query);
    }

    public LiveData<java.util.List<Note>> searchNotesByBody(String query) {
        return noteDao.searchNotesByBody(query);
    }

    public LiveData<java.util.List<Note>> searchNotesByTitleOrBody(String query) {
        return noteDao.searchNotesByTitleOrBody(query);
    }

    public LiveData<java.util.List<Note>> getNotesByNotebookId(int notebookId) {
        return noteDao.getNotesByNotebookId(notebookId);
    }

    public LiveData<List<Note>> getNotesByNotebookIdSorted(int notebookId, String sortBy, boolean ascending) {
        if ("title".equals(sortBy)) {
            return ascending ? noteDao.getNotesByNotebookIdSortedByTitleAsc(notebookId)
                    : noteDao.getNotesByNotebookIdSortedByTitleDesc(notebookId);
        } else {
            return ascending ? noteDao.getNotesByNotebookIdSortedByDateAsc(notebookId)
                    : noteDao.getNotesByNotebookIdSortedByDateDesc(notebookId);
        }
    }


    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

}
