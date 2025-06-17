package com.example.daftarak.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.daftarak.data.model.Note;
import com.example.daftarak.data.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
    }

    public LiveData<List<Note>> getNotesSortedByDateAsc() {
        return repository.getNotesSortedByDateAsc();
    }

    public LiveData<List<Note>> getNotesSortedByDateDesc() {
        return repository.getNotesSortedByDateDesc();
    }

    public LiveData<List<Note>> getNotesSortedByTitleAsc() {
        return repository.getNotesSortedByTitleAsc();
    }

    public LiveData<List<Note>> getNotesSortedByTitleDesc() {
        return repository.getNotesSortedByTitleDesc();
    }

    public LiveData<List<Note>> searchNotesByTitle(String query) {
        return repository.searchNotesByTitle(query);
    }

    public LiveData<List<Note>> searchNotesByBody(String query) {
        return repository.searchNotesByBody(query);
    }

    public LiveData<List<Note>> searchNotesByTitleOrBody(String query) {
        return repository.searchNotesByTitleOrBody(query);
    }

    public LiveData<List<Note>> getNotesByNotebookId(int notebookId) {
        return repository.getNotesByNotebookId(notebookId);
    }

    public void insert(Note note) {
        repository.insertNote(note);
    }

    public void update(Note note) {
        repository.updateNote(note);
    }

    public void delete(Note note) {
        repository.deleteNote(note);
    }
}
