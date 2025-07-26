package com.mnazar.daftarak.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mnazar.daftarak.data.model.Notebook;
import com.mnazar.daftarak.data.repository.NotebookRepository;

import java.util.List;

public class NotebookViewModel extends AndroidViewModel {

    private final NotebookRepository repository;
    private final LiveData<List<Notebook>> allNotebooks;

    public NotebookViewModel(@NonNull Application application) {
        super(application);
        repository = new NotebookRepository(application);
        allNotebooks = repository.getAllNotebooks();
    }

    public LiveData<List<Notebook>> getAllNotebooks() {
        return allNotebooks;
    }

    public void insert(Notebook notebook) {
        repository.insertNotebook(notebook);
    }

    public void update(Notebook notebook) {
        repository.updateNotebook(notebook);
    }

    public void delete(Notebook notebook) {
        repository.deleteNotebook(notebook);
    }
}
