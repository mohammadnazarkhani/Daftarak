package com.mnazar.daftarak.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mnazar.daftarak.data.dao.NotebookDao;
import com.mnazar.daftarak.data.database.AppDatabase;
import com.mnazar.daftarak.data.model.Notebook;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotebookRepository {

    private final NotebookDao notebookDao;
    private final ExecutorService executorService;

    public NotebookRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        notebookDao = db.notebookDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertNotebook(Notebook notebook) {
        executorService.execute(() -> notebookDao.insertNotebook(notebook));
    }

    public void updateNotebook(Notebook notebook) {
        executorService.execute(() -> notebookDao.updateNotebook(notebook));
    }

    public void deleteNotebook(Notebook notebook) {
        executorService.execute(() -> notebookDao.deleteNotebook(notebook));
    }

    public LiveData<List<Notebook>> getAllNotebooks() {
        return notebookDao.getAllNotebooks();
    }

    public LiveData<Notebook> getNotebookById(int id) {
        return notebookDao.getNotebookById(id);
    }
}
